'use strict';
describe("Principal test", function() {
	var Principal, Account, AuthJWTProvider, $rootScope, ROLES, $q;
	var token = "JWT token";
    var event = "auth-checked";
    var identity = { name: 'Test User', slug: 'test-user' };
    var reject = { data: {}, status: 401 };

	beforeEach(angular.mock.module('app.auth'));

	beforeEach(inject(function(_$rootScope_, _$q_, _Account_, _AuthJWTProvider_, _ROLES_, _Principal_) {
		$rootScope = _$rootScope_;
		$q = _$q_;
		Account = _Account_;
		AuthJWTProvider = _AuthJWTProvider_;
		ROLES = _ROLES_;
		Principal = _Principal_;
	}));

	function resolveIdentity(callBroadcast) {
        spyOn(AuthJWTProvider, 'getToken').and.returnValue(token);
        spyOn(Account, 'get').and.callFake(function() {
            return {
                $promise: $q.when(identity)
            }
        });
		if (callBroadcast) {
			spyOn($rootScope, '$broadcast').and.callThrough();
		} else {
			spyOn($rootScope, '$broadcast');
		}
	}

	function rejectIdentity() {
        spyOn(AuthJWTProvider, 'getToken').and.returnValue(token);
        spyOn(Account, 'get').and.callFake(function() {
            return {
                $promise: $q.reject(reject)
            }
        });
        spyOn($rootScope, '$broadcast');
	}

	describe("has authority", function() {
		it("shoud return false when the user is not authenticated", function() {
            expect(Principal._hasAuthority(ROLES.admin)).toBeFalsy();
		});

        it("shoud return false when the user don't have a authorities set", function() {
			identity.authorities = null;
            resolveIdentity();
			Principal.identity();
			$rootScope.$apply();

			expect(Principal.getIdentity()).toEqual(identity);
			expect(Principal._hasAuthority(ROLES.regular)).toBeFalsy();
        });

		it("shoud return true when the user has the given authority", function() {
            identity.authorities = [ROLES.admin];
            resolveIdentity();
            Principal.identity();
			$rootScope.$apply();

			expect(Principal.getIdentity()).toEqual(identity);
			expect(Principal._hasAuthority(ROLES.admin)).toBeTruthy();
		});

        it("shoud return false when the user don't have the given authority", function() {
            identity.authorities = [ROLES.regular];
            resolveIdentity();
            Principal.identity();
			$rootScope.$apply();

			expect(Principal.getIdentity()).toEqual(identity);
			expect(Principal._hasAuthority(ROLES.admin)).toBeFalsy();
        });

		function onReject() {
            fail("identity rejected");
		}
	});

	describe("is admin", function() {
		it("should return true when the user has a admin role", function() {
            identity.authorities = [ROLES.admin];
            resolveIdentity();
            Principal.identity();
			$rootScope.$apply();

			expect(Principal.getIdentity()).toEqual(identity);
			expect(Principal.isAdmin()).toBeTruthy();
		});
	});

    describe("is admin", function() {
        it("should return true when the user has a regular role", function() {
            identity.authorities = [ROLES.regular];
            resolveIdentity();
            Principal.identity();
			$rootScope.$apply();

			expect(Principal.getIdentity()).toEqual(identity);
			expect(Principal.isRegularUser()).toBeTruthy();
        });
    });

	describe("identity", function() {
		it("should not send a http request when the token is not in the local storage", function() {
			spyOn(AuthJWTProvider, 'getToken').and.returnValue(null);
			spyOn(Account, 'get');

			Principal.identity()
                .then(onResolve, onReject);
            $rootScope.$apply();

			function onResolve(data) {
				fail("result not rejected");
			}

			function onReject(data) {
				expect(Principal.isAuthenticated()).toBeFalsy();
				expect(AuthJWTProvider.getToken).toHaveBeenCalled();
				expect(Account.get).not.toHaveBeenCalled();
			}
		});

		it("should set the identity when the http request is successful", function() {
			resolveIdentity();
			Principal.identity()
				.then(onResolve, onReject);
            $rootScope.$apply();

			function onResolve(data) {
				expect(AuthJWTProvider.getToken).toHaveBeenCalled();
				expect(Account.get).toHaveBeenCalled();
				expect($rootScope.$broadcast).toHaveBeenCalledWith(event);
				expect(Principal.isAuthenticated()).toBeTruthy();
                expect(data).toEqual(identity);
                expect(Principal.getIdentity()).toEqual(identity);
				expect(Principal._isAuthenticationChecked()).toBeTruthy();
			}

			function onReject(data) {
				fail("http was resolved, but the function resulted with a reject");
			}
		});

		it("should set the identoty to null when the http request failed", function() {
			rejectIdentity();
            Principal.identity()
                .then(onResolve, onReject);
            $rootScope.$apply();

			function onResolve() {
				fail("http request rejected, but the function resulted with a resolve");
			}

			function onReject(data) {
				expect(AuthJWTProvider.getToken).toHaveBeenCalled();
				expect(Account.get).toHaveBeenCalled();
				expect($rootScope.$broadcast).toHaveBeenCalledWith(event);
				expect(Principal.isAuthenticated()).toBeFalsy();
				expect(Principal.getIdentity()).toBeNull();
				expect(data).toEqual(reject);
                expect(Principal._isAuthenticationChecked()).toBeTruthy();
			}
		});

		it("should not send a request if the identity is not null", function() {
			resolveIdentity();
            Principal.identity();
            $rootScope.$apply();

			expect(Principal._isAuthenticationChecked()).toBeTruthy();

			// reset the mocks
			AuthJWTProvider.getToken.calls.reset();
			Account.get.calls.reset();
			$rootScope.$broadcast.calls.reset();

			Principal.identity()
				.then(onResolve, onReject);
			$rootScope.$apply();

			function onResolve(data) {
				expect(data).toEqual(identity);
				expect(AuthJWTProvider.getToken.calls.any()).toBeFalsy();
				expect(Account.get.calls.any()).toBeFalsy();
				expect($rootScope.$broadcast.calls.any()).toBeTruthy();
			}

			function onReject() {
				fail("identity not null, but the function resulted with a reject");
			}
		});
	});

	describe("resolve identity", function() {
		it("should resolve instantly when the authentication is already checked", function() {
			spyOn($rootScope, '$on');
			resolveIdentity();
			Principal.identity()
				.then(onResolve, onReject);
            $rootScope.$apply();

			function onResolve() {
				expect(Principal.getIdentity()).toEqual(identity);
				expect(Principal._isAuthenticationChecked).toBeTruthy();
				Principal.resolveIdentity()
					.then(_onResolve, onIdentotyResolvingRejected);

				function _onResolve(data) {
					expect(data).toEqual(identity);
					expect($rootScope.$on).not.toHaveBeenCalled();
				}
			}

			function onReject() {
				fail("identity rejected");
			}
		});

		it("should register a watcher and resolve after the watcher is called", function() {
			spyOn($rootScope, '$on').and.callThrough();
			resolveIdentity(true);
			Principal.resolveIdentity()
				.then(onResolve, onIdentotyResolvingRejected);
			Principal.identity();
			$rootScope.$apply();

			function onResolve(data) {
				expect($rootScope.$on).toHaveBeenCalled();
				expect(data).toEqual(identity);
			}
		});

		function onIdentotyResolvingRejected() {
			fail("resolving the identity rejected");
		}
	});

	describe("invalidate", function() {
		it("should invalidate the user", function() {
			resolveIdentity();
			Principal.identity();
			$rootScope.$apply();

			expect(Principal.isAuthenticated()).toBeTruthy();
			expect(Principal.getIdentity()).toEqual(identity);
			Principal.invalidate();
			expect(Principal.isAuthenticated()).toBeFalsy();
			expect(Principal.getIdentity()).toBeNull();
		});
	});
});