(function() {
	'use strict';

	angular
		.module('app.auth')
		.factory('RefreshToken', RefreshToken);

	RefreshToken.$inject = ['$http'];

	function RefreshToken($http) {
		var resourceUrl = "/api/auth/refresh";
		var service = {
			refresh: refresh
		};

		return service;

		function refresh() {
			return $http.get(resourceUrl, {ignoreTokenRefreshInterceptor: true});
		}
	}
})();
