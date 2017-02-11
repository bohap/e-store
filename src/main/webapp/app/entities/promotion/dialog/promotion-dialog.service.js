(function() {
	'use strict';

	angular
		.module('app.promotion.dialog')
		.factory('PromotionDialog', PromotionDialog);

	PromotionDialog.$inject = ['$uibModal'];

	function PromotionDialog($uibModal) {
		var service = {
			open: open
		};

		var modalInstance = null;

		return service;

		function open(book) {
			modalInstance = $uibModal.open({
				templateUrl: 'app/entities/promotion/dialog/promotion.html',
				controller: 'PromotionCreateController',
				controllerAs: 'vm',
				resolve: {
					book: function() {
						return book;
					}
				}
			});

			modalInstance.result
				.then(resetModal, resetModal);
		}

		function resetModal() {
			modalInstance = null;
		}
	}
})();
