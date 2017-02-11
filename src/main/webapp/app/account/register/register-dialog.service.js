(function() {
	'use strict';

	angular
		.module('app.register')
		.factory('RegisterDialog', RegisterDialog);

	RegisterDialog.$inject = ['$uibModal'];

	function RegisterDialog($uibModal) {
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
				templateUrl: 'app/account/register/register.html',
				controller: 'RegisterController',
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
