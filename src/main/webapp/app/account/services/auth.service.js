(function() {
	'use strict';

	angular
		.module('app')
		.factory('Auth', Auth);

	Auth.$inject = ['$q', 'AuthJWTProvider', 'Login', 'Register', 'RefreshToken', 'Principal',
		'$rootScope', 'EVENTS'];

	function Auth($q, AuthJWTProvider, Login, Register, RefreshToken, Principal, $rootScope, EVENTS) {
		var service = {
			login: login,
			register: register,
			refresh: refresh,
			logout: logout
		};

		return service;

		function login(credentials) {
			var deferred = $q.defer();
			Login.login(credentials)
				.then(loginSuccess, loginFailed);

			function loginSuccess(response) {
				authenticationSuccess(response, deferred);
			}

			function loginFailed(response) {
				deferred.reject(response.data);
			}

			return deferred.promise;
		}

		function register(account) {
			var deferred = $q.defer();
			Register.register(account)
				.then(registrationSuccess, registrationFailed);

			function registrationSuccess(response) {
				authenticationSuccess(response, deferred);
			}

			function registrationFailed(response) {
				deferred.reject(response.data);
			}

			return deferred.promise;
		}

		function authenticationSuccess(response, deferred) {
			AuthJWTProvider.storeToken(response);

			// Get the user account
			Principal.identity()
				.then(identityResolveSuccess, identityResolveFailed);

			function identityResolveSuccess(identity) {
				$rootScope.$broadcast(EVENTS.authenticationSuccess);
				deferred.resolve(identity);
			}

			function identityResolveFailed(response) {
				deferred.reject(response);
			}
		}

		function refresh() {
			var deferred = $q.defer();
			var token = AuthJWTProvider.getToken();
			if (angular.isUndefined(token) || token === null) {
				deferred.reject(false);
			}
			RefreshToken.refresh()
				.then(refreshSuccess, refreshFailed);

			function refreshSuccess(response) {
				AuthJWTProvider.storeToken(response);
				deferred.resolve(response);
			}

			function refreshFailed(response) {
				AuthJWTProvider.deleteToken();
				deferred.reject(response);
			}

			return deferred.promise;
		}

		function logout() {
			AuthJWTProvider.deleteToken();
			Principal.invalidate();
			$rootScope.$broadcast(EVENTS.logoutSuccess);
		}
	}
})();
