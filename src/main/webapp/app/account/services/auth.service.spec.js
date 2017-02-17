'use strict';
describe("Auth Service test", function() {
	var Auth, $q, AuthJWTProvider, Login, Register, RefreshToken, Principal, $rootScope, EVENTS;
	var identity = { name: 'Test User', slug: 'test-user' };

	beforeEach(angular.mock.module('app.auth'));

	beforeEach(inject(function(_$rootScope_, _$q_, _AuthJWTProvider_, _Login_, _Register_,
								 _RefreshToken_, _Principal_, _EVENTS_, _Auth_) {
		$rootScope = _$rootScope_;
		$q = _$q_;
		AuthJWTProvider = _AuthJWTProvider_;
		Login = _Login_;
		Register = _Register_;
		RefreshToken = _RefreshToken_;
		Principal = _Principal_;
		EVENTS = _EVENTS_;
		Auth = _Auth_;
	}));

	function initMocks(storeTokenSuccess, identityResponse) {
        if (storeTokenSuccess === undefined) {
            spyOn(AuthJWTProvider, 'storeToken');
        } else {
            spyOn(AuthJWTProvider, 'storeToken').and.returnValue(storeTokenSuccess);
        }

        if (identityResponse === undefined) {
            spyOn(Principal, 'identity');
        } else {
            spyOn(Principal, 'identity').and.returnValue(identityResponse);
        }

        spyOn($rootScope, '$broadcast');
	}

	describe("login", function() {
        var credentials = { email: 'test@user.com', password: 'pass' };
        var successResponse = { token: 'JWT token' };
        var failedResponse = { status: 401, data: { msg: "Bad Credentials" } };

		function initLoginMockResponse(response) {
            if (response === undefined) {
                spyOn(Login, 'login');
            } else {
                spyOn(Login, 'login').and.returnValue(response);
            }
		}

		it("should resolve the identity when the login is successful", function() {
            initLoginMockResponse($q.when(successResponse));
			initMocks(true, $q.when(identity));

			Auth.login(credentials)
				.then(onResolve, onReject);
			$rootScope.$apply();

			function onResolve(data) {
                expect(Login.login).toHaveBeenCalledWith(credentials);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).toHaveBeenCalled();
                expect($rootScope.$broadcast).toHaveBeenCalledWith(EVENTS.authenticationSuccess);
				expect(data).toEqual(identity);
			}

			function onReject(data) {
				fail("rejected");
			}
		});

		it("should reject the response when logging failed", function() {
            initLoginMockResponse($q.reject(failedResponse));
            initMocks();

            Auth.login(credentials)
                .then(onResolve, onReject);
            $rootScope.$apply();

			function onResolve() {
				fail("resolved");
			}

			function onReject(data) {
                expect(Login.login).toHaveBeenCalledWith(credentials);
                expect(AuthJWTProvider.storeToken).not.toHaveBeenCalled();
                expect(Principal.identity).not.toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual(failedResponse.data);
			}
		});

		it("should reject the response when storing the token failed", function() {
            initLoginMockResponse($q.when(successResponse));
			initMocks(false);

            Auth.login(credentials)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
				fail("resolved");
            }

            function onReject(data) {
                expect(Login.login).toHaveBeenCalledWith(credentials);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).not.toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual({ error: "Token Extraction from the response failed" });
            }
		});

        it("should reject the response when resolving the identity failed", function() {
            initLoginMockResponse($q.when(successResponse));
            initMocks(true, $q.reject(failedResponse));

            Auth.login(credentials)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                fail("resolved");
            }

            function onReject(data) {
                expect(Login.login).toHaveBeenCalledWith(credentials);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual(failedResponse.data);
            }
		});
	});

	describe("register", function() {
        var account = { name: 'Test User', email: 'test@user.com', password: 'pass' };
        var successResponse = { token: 'JWT token' };
        var failedResponse = { status: 400, data: { msg: "Bad Input" } };

        function initRegisterMockResponse(response) {
            if (response === undefined) {
                spyOn(Register, 'register');
            } else {
                spyOn(Register, 'register').and.returnValue(response);
            }
        }

        it("should resolve the identity when the registration is successful", function() {
            initRegisterMockResponse($q.when(successResponse));
            initMocks(true, $q.when(identity));

            Auth.register(account)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                expect(Register.register).toHaveBeenCalledWith(account);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).toHaveBeenCalled();
                expect($rootScope.$broadcast).toHaveBeenCalledWith(EVENTS.authenticationSuccess);
                expect(data).toEqual(identity);
            }

            function onReject(data) {
                fail("rejected");
            }
        });

        it("should reject the response when registration failed", function() {
            initRegisterMockResponse($q.reject(failedResponse));
            initMocks();

            Auth.register(account)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve() {
                fail("resolved");
            }

            function onReject(data) {
                expect(Register.register).toHaveBeenCalledWith(account);
                expect(AuthJWTProvider.storeToken).not.toHaveBeenCalled();
                expect(Principal.identity).not.toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual(failedResponse.data);
            }
        });

        it("should reject the response when storing the token failed", function() {
            initRegisterMockResponse($q.when(successResponse));
            initMocks(false);

            Auth.register(account)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                fail("resolved");
            }

            function onReject(data) {
                expect(Register.register).toHaveBeenCalledWith(account);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).not.toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual({ error: "Token Extraction from the response failed" });
            }
        });

        it("should reject the response when resolving the identity failed", function() {
            initRegisterMockResponse($q.when(successResponse));
            initMocks(true, $q.reject(failedResponse));

            Auth.register(account)
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                fail("resolved");
            }

            function onReject(data) {
                expect(Register.register).toHaveBeenCalledWith(account);
                expect(AuthJWTProvider.storeToken).toHaveBeenCalledWith(successResponse);
                expect(Principal.identity).toHaveBeenCalled();
                expect($rootScope.$broadcast).not.toHaveBeenCalled();
                expect(data).toEqual(failedResponse.data);
            }
        });
	});

	describe("refresh", function() {
		it("should not send a request if the token is not stored", function() {
			spyOn(AuthJWTProvider, 'getToken');
            spyOn(RefreshToken, 'refresh');

			Auth.refresh()
				.then(onResolve, onReject);
			$rootScope.$apply();

			function onResolve(data) {
				fail("resolved");
			}

			function onReject(data) {
				expect(AuthJWTProvider.getToken).toHaveBeenCalled();
				expect(RefreshToken.refresh).not.toHaveBeenCalled();
				expect(data).toBeFalsy();
			}
		});

		it("should reject the response when refreshing failed", function() {
			var response = { status: 401, data: {} };

            spyOn(AuthJWTProvider, 'getToken').and.returnValue("token");
            spyOn(AuthJWTProvider, 'deleteToken');
            spyOn(RefreshToken, 'refresh').and.returnValue($q.reject(response));

            Auth.refresh()
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                fail("resolved");
            }

            function onReject(data) {
                expect(AuthJWTProvider.getToken).toHaveBeenCalled();
				expect(AuthJWTProvider.deleteToken).toHaveBeenCalled();
                expect(RefreshToken.refresh).toHaveBeenCalled();
                expect(data).toEqual(response);
            }
		});

        it("should resolve the response when refreshing successed", function() {
            var response = { status: 200, data: { msg: "Refreshed" } };

            spyOn(AuthJWTProvider, 'getToken').and.returnValue("token");
            spyOn(AuthJWTProvider, 'storeToken');
            spyOn(RefreshToken, 'refresh').and.returnValue($q.when(response));

            Auth.refresh()
                .then(onResolve, onReject);
            $rootScope.$apply();

            function onResolve(data) {
                expect(AuthJWTProvider.getToken).toHaveBeenCalled();
                expect(RefreshToken.refresh).toHaveBeenCalled();
				expect(AuthJWTProvider.storeToken).toHaveBeenCalled();
                expect(data).toEqual(response);
            }

            function onReject(data) {
                fail("resolved");
            }
        });
	});

	describe("logout", function() {
		it("should remove the token and invalidate the user", function() {
            spyOn(AuthJWTProvider, 'deleteToken');
            spyOn(Principal, 'invalidate');
            spyOn($rootScope, '$broadcast');

            Auth.logout();

            expect(AuthJWTProvider.deleteToken).toHaveBeenCalled();
            expect(Principal.invalidate).toHaveBeenCalled();
            expect($rootScope.$broadcast).toHaveBeenCalledWith(EVENTS.logoutSuccess);
		});
	});
});