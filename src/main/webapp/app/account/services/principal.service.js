(function() {
	'use strict';

	angular
		.module('app')
		.factory('Principal', Principal);

	Principal.$inject = ['$q', 'Account', 'AuthJWTProvider', '$rootScope'];

	function Principal($q, Account, AuthJWTProvider, $rootScope) {
		var _identity = null;
		var autheticated = false;
		var event = "auth-checked";
		var authChecked = false;
		var service = {
			isAuthenticated: isAuthenticated,
			getIdentity: getIdentity,
			identity: identity,
			resolveIdentity: resolveIdentity,
			invalidate: invalidate
		};

		return service;

		function isAuthenticated() {
			return autheticated;
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
				var resolve = _identity !== null ? _identity : null;
				deferred.resolve(resolve);
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
