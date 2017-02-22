(function() {
	'use strict';

	angular
		.module('app.handler')
		.factory('errorHandler', errorHandler);

	errorHandler.$inject = ['$injector'];

	function errorHandler($injector) {
		var service = {
			handleNotFound: handleNotFound,
			handleNotAuthenticated: handleNotAuthenticated,
			handleNotAuthorized: handleNotAuthorized,
			handleInternalServerError: handleInternalServerError
		};

		return service;

		function handleNotFound() {
			var notify = $injector.get('ToastrNotify');
			notify.showNotFound();
			// go to nod found route
		}

		function handleNotAuthenticated() {
			var notify = $injector.get('ToastrNotify');
			notify.showNotAuthenticated();

			// Show the login dialog
			var LoginDialog = $injector.get('LoginDialog');
			LoginDialog.open();
		}

		function handleNotAuthorized() {
			var notify = $injector.get('ToastrNotify');
			notify.showNotAuthorized();
		}

		function handleInternalServerError() {
			var notify = $injector.get('ToastrNotify');
			notify.showInternalServerError();
		}
	}
})();
