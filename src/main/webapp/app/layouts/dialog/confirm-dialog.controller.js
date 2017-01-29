(function() {
	'use strict';

	angular
		.module('app')
		.controller('ConfirmDialogController', ConfirmDialogController);

	ConfirmDialogController.$inject = ['$uibModalInstance', 'message'];

	function ConfirmDialogController($uibModalInstance, message) {
		var vm = this;

		vm.confirm = confirm;
		vm.cancel = cancel;
		vm.message = message;

		function confirm() {
			$uibModalInstance.close('confirm');
		}

		function cancel() {
			$uibModalInstance.dismiss('cancel');
		}
	}
})();
