(function() {
	'use strict';

	angular
		.module('app.notify')
		.factory('ToastrNotify', ToastrNotify);

	ToastrNotify.$inject = ['toastr', 'MESSAGES'];

	function ToastrNotify(toastr, MESSAGES) {
		var service = {
			showNotFound: showNotFound,
			showNotAuthenticated: showNotAuthenticated,
			showNotAuthorized: showNotAuthorized,
			showInternalServerError: showInternalServerError,
			error: error,
			success: success
		};

		return service;

		function showNotFound() {
			var msg = MESSAGES.notFound;
			toastr.error(msg.body, msg.title);
		}

		function showNotAuthenticated() {
			var msg = MESSAGES.notAuthenticated;
			toastr.error(msg.body, msg.title);
		}

		function showNotAuthorized() {
			var msg = MESSAGES.notAuthorized;
			toastr.error(msg.body, msg.title);
		}

		function showInternalServerError() {
			var msg = MESSAGES.internalServerError;
			toastr.error(msg.body, msg.title);
		}

		function error(message, title) {
			toastr.error(message, title);
		}

		function success(message, title) {
			toastr.success(message, title);
		}
	}
})();
