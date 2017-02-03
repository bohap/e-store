(function() {
	'use strict';

	angular
		.module('app')
		.factory('Principal', Principal);

	Principal.$inject = ['$q', 'Account', 'AuthJWTProvider', '$rootScope', 'ROLES'];

	function Principal($q, Account, AuthJWTProvider, $rootScope, ROLES) {
		var _identity = null;
		var autheticated = false;

		var event = "auth-checked";
		var authChecked = false;
		var service = {
			isAuthenticated: isAuthenticated,
			isAdmin: isAdmin,
			isRegularUser: isRegularUser,
			getIdentity: getIdentity,
			identity: identity,
			resolveIdentity: resolveIdentity,
			invalidate: invalidate
		};

		return service;

		function isAuthenticated() {
			return autheticated;
		}

		function isAdmin() {
			return hasAuthority(ROLES.admin);
		}

		function isRegularUser() {
			return hasAuthority(ROLES.regular);
		}

		function hasAuthority(authority) {
			if (!autheticated) {
				return false;
			}
			var authorities = _identity.authorities;
			if (angular.isUndefined(authorities) || authorities === null) {
				return false;
			}

			for (var i = 0; i < authorities.length; i++) {
				if (authorities[i] === authority) {
					return true;
				}
			}
			return false;
		}

		function getIdentity() {
			return _identity;
		}

		function identity() {
			var deferred = $q.defer();
			if (_identity !== null) {
				resolve(_identity);
			}

			var token = AuthJWTProvider.getToken();
			if (angular.isDefined(token) && token !== null) {
				Account.get().$promise
					.then(identityResolveSuccess, identityResolveFailed);
			} else {
				reject({error: "Token not exists"});
			}

			function identityResolveSuccess(response) {
				_identity = response;
				autheticated = true;
				resolve(_identity);
			}

			function identityResolveFailed(response) {
				_identity = null;
				autheticated = false;
				reject(response);
			}

			function resolve(data) {
				authChecked = true;
				$rootScope.$broadcast(event);
				deferred.resolve(data);
			}

			function reject(data) {
				authChecked = true;
				$rootScope.$broadcast(event);
				deferred.reject(data);
			}

			return deferred.promise;
		}

		function resolveIdentity() {
			var deferred = $q.defer();
			if (authChecked) {
				deferred.resolve(_identity);
			}
			var unwatch = $rootScope.$on(event, function(event, data) {
				deferred.resolve(_identity);
				unwatch();
			});

			return deferred.promise;
		}

		function invalidate() {
			_identity = null;
			autheticated = false;
		}
	}
})();
