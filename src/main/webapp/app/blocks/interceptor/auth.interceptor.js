(function() {
	'use strict';

	angular
		.module('app')
		.factory('authInterceptor', authInterceptor);

	authInterceptor.$inject = ['$localStorage'];

	function authInterceptor($localStorage) {
		return {
			request: request
		};

		function request(config) {
			var url = config.url;
			if (url.startsWith('/api')) {
				var token = $localStorage.jwt;
				if (angular.isDefined(token) && token !== null) {
					config.headers.Authorization = "Bearer " + token;
				}
			}
			return config;
		}
	}
})();
