(function() {
	'use strict';

	angular
		.module('app')
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
			var messageUtil = $injector.get('MessageUtil');
			messageUtil.showNotFound();
			// go to nod found route
		}

		function handleNotAuthenticated() {
			var messageUtil = $injector.get('MessageUtil');
			messageUtil.showNotAuthenticated();

			// Show the login dialog
			var LoginDialog = $injector.get('LoginDialog');
			LoginDialog.open();
		}

		function handleNotAuthorized() {
			var messageUtil = $injector.get('MessageUtil');
			messageUtil.showNotAuthorized();
		}

		function handleInternalServerError() {
			var messageUtil = $injector.get('MessageUtil');
			messageUtil.showInternalServerError();
		}
	}
})();
