(function() {
	'use strict';

	angular
		.module('app')
		.factory('errorHandler', errorHandler);

	errorHandler.$inject = ['$injector', 'MESSAGES'];

	function errorHandler($injector, MESSAGES) {
		var service = {
			handleNotFound: handleNotFound,
			handleNotAuthenticated: handleNotAuthenticated,
			handleNotAuthorized: handleNotAuthorized,
			handleInternalServerError: handleInternalServerError
		};

		return service;

		function showToast(msg) {
			var toastr = $injector.get('toastr');
			toastr.error(msg.body, msg.title);
		}

		function handleNotFound() {
			showToast(MESSAGES.notFound);
			// go to nod found route
		}

		function handleNotAuthenticated() {
			var LoginDialog = $injector.get('LoginDialog');
			showToast(MESSAGES.notAuthenticated);
			LoginDialog.open();
		}

		function handleNotAuthorized() {
			showToast(MESSAGES.notAuthorized);
		}

		function handleInternalServerError() {
			showToast(MESSAGES.internalServerError);
		}
	}
})();
