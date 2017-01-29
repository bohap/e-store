(function() {
	'use strict';

	angular
		.module('app')
		.factory('ConfirmDialog', ConfirmDialog);

	ConfirmDialog.$inject = ['$uibModal', '$q'];

	function ConfirmDialog($uibModal, $q) {
		var service = {
			open: open
		};

		var modalInstance = null;

		return service;

		function open(message) {
			if (modalInstance !== null) {
				return;
			}
			var deferred = $q.defer();

			modalInstance = $uibModal.open({
				templateUrl: 'app/layouts/dialog/confirm-dialog.html',
				controller: 'ConfirmDialogController',
				controllerAs: 'vm',
				resolve: {
					message: function() {
						return message;
					}
				}
			});

			modalInstance.result
				.then(onModalClosed, onModalDismissed);

			return deferred.promise;

			function onModalClosed(result) {
				modalInstance = null;
				deferred.resolve("Confirm");
			}

			function onModalDismissed(result) {
				modalInstance = null;
				deferred.reject("Canceled");
			}
		}
	}
})();
