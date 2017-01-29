(function() {
	'use strict';

	angular
		.module('app')
		.factory('errorInterceptor', errorInterceptor);

	errorInterceptor.$inject = ['$q', 'errorHandler', '$log'];

	function errorInterceptor($q, errorHandler, $log) {
		var service = {
			responseError: responseError
		};

		return service;

		function responseError(response) {
			if (!shouldIngnore(response)) {
				var method = {
					401: "handleNotAuthenticated",
					403: "handleNotAuthorized",
					404: "handleNotFound",
					500: "handleInternalServerError"
				}[response.status];

				if (angular.isDefined(method)) {
					errorHandler[method]();
				}
			}
			return $q.reject(response);
		}

		function shouldIngnore(response) {
			return response.status === 401 &&
				response.config.url.startsWith("/api/auth");
		}
	}
})();
