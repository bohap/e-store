(function() {
	'use strict';

	angular
		.module('app.order.dialog')
		.factory("OrderCreateDialog", OrderCreateDialog);

	OrderCreateDialog.$inject = ['$uibModal', '$q'];

	function OrderCreateDialog($uibModal, $q) {
		var service = {
			open: open
		};

		var modalInstance = null;

		return service;

		function open(items) {
			if (modalInstance !== null) {
				return;
			}

			var deferred = $q.defer();

			modalInstance = $uibModal.open({
				templateUrl: 'app/entities/order/dialog/order-create.html',
				size: 'lg',
				backdrop  : 'static',
				keyboard  : false,
				controller: 'OrderCreateController',
				controllerAs: 'vm',
				resolve: {
					items: function() {
						return items;
					}
				}
			});

			modalInstance.result
				.then(onDialogClosed, onDialogCanceled);

			function onDialogClosed() {
				modalInstance = null;
				deferred.resolve(true);
			}

			function onDialogCanceled() {
				modalInstance = null;
				deferred.reject(false);
			}

			return deferred.promise;
		}
	}
})();
