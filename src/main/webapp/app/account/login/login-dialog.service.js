(function() {
	'use strict';

	angular
		.module('app.login')
		.factory('LoginDialog', LoginDialog);

	LoginDialog.$inject = ['$uibModal'];

	function LoginDialog($uibModal) {
		var service = {
			open: open
		};

		var modalInstance = null;

		return service;

		function open() {
			if (modalInstance !== null) {
				return;
			}
			modalInstance = $uibModal.open({
				templateUrl: 'app/account/login/login.html',
				controller: 'LoginController',
				controllerAs: 'vm'
			});
			modalInstance.result
				.then(resetModal, resetModal);
		}

		function resetModal() {
			modalInstance = null;
		}
	}
})();
