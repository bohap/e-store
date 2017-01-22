(function() {
	'use strict';

	angular
		.module('app')
		.factory('errorInterceptor', errorInterceptor);

	errorInterceptor.$inject = ['$q', 'errorHandler'];

	function errorInterceptor($q, errorHandler) {
		var service = {
			responseError: responseError
		};

		return service;

		function responseError(response) {
			if (!shouldIngnore(response)) {
				var method = {
					404: "handleNotFound",
					403: "handleNotAuthorized",
					401: "handleNotAuthenticated",
					500: "handleInternalServerError"
				}[response.status];
				errorHandler[method]();
			}
			return $q.reject(response);
		}

		function shouldIngnore(response) {
			return response.status === 401 &&
				response.config.url.startsWith("/api/auth");
		}
	}
})();
