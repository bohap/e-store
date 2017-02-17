(function() {
	'use strict';

	angular
		.module('app.auth')
		.factory('AuthJWTProvider', AuthJWTProvider);

	AuthJWTProvider.$inject = ['$localStorage', '$q'];

	function AuthJWTProvider($localStorage, $q) {
		var service = {
			getToken: getToken,
			storeToken: storeToken,
			deleteToken: deleteToken,
		};

		return service;

		function getToken() {
			return $localStorage.jwt;
		}

		function storeToken(httpResponse) {
			if (angular.isUndefined(httpResponse) || httpResponse === null) {
				return false;
			}
			var token = httpResponse.data.jwtToken;
			if (angular.isUndefined(token) || token === null) {
				var bearer = httpResponse.headers('Authorization');
				if (angular.isDefined(bearer) && bearer !== null &&
						bearer.slice(0, 7) === 'Bearer ') {
					token = bearer.slice(7, bearer.length);
				}
			}
			if (angular.isDefined(token) && token !== null) {
				$localStorage.jwt = token;
				return true;
			}
			return false;
		}

		function deleteToken() {
			delete $localStorage.jwt;
		}
	}
})();

