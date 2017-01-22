(function() {
	'use strict';

	angular
		.module('app')
		.factory('tokenRefreshInterceptor', tokenRefreshInterceptor);

	tokenRefreshInterceptor.$inject = ['$q', '$injector', 'AuthJWTProvider'];

	function tokenRefreshInterceptor($q, $injector, AuthJWTProvider) {
		var service = {
			responseError: responseError
		};

		return service;

		function responseError(response) {
			var Auth = $injector.get('Auth');
			var $http = $injector.get('$http');
			var status = response.status;
			var token = AuthJWTProvider.getToken();
			var ignore = response.config.ignoreTokenRefreshInterceptor;
			if (!ignore && status === 401 && angular.isDefined(token) && token !== null) {
				return Auth.refresh()
					.then(refreshSuccess, refreshFailed);
			}

			return $q.reject(response);

			function refreshSuccess(data) {
				return $http(response.config);
			}

			function refreshFailed(data) {
				return $q.reject(response);
			}
		}
	}
})();
