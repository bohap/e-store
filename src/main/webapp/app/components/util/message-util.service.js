(function() {
	'use strict';

	angular
		.module('app')
		.factory('MessageUtil', MessageUtil);

	MessageUtil.$inject = ['toastr', 'MESSAGES'];

	function MessageUtil(toastr, MESSAGES) {
		var service = {
			showNotFound: showNotFound,
			showNotAuthenticated: showNotAuthenticated,
			showNotAuthorized: showNotAuthorized,
			showInternalServerError: showInternalServerError
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
	}
})();
