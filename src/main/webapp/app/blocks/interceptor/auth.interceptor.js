(function() {
	'use strict';

	angular
		.module('app')
		.factory('authInterceptor', authInterceptor);

	authInterceptor.$inject = ['AuthJWTProvider'];

	function authInterceptor(AuthJWTProvider) {
		return {
			request: request
		};

		function request(config) {
			var url = config.url;
			if (url.startsWith('/api')) {
				var token = AuthJWTProvider.getToken();
				if (angular.isDefined(token) && token !== null) {
					config.headers.Authorization = "Bearer " + token;
				}
			}
			return config;
		}
	}
})();
