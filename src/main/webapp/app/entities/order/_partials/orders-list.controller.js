(function() {
	'use strict';

	angular
		.module('app')
		.controller('OrderListController', OrderListController);

	OrderListController.$inject = ['Principal', 'Order', 'toastr'];

	function OrderListController(Principal, Order, toastr) {
		var vm = this;

		vm.canFinishOrder = canFinishOrder;
		vm.finish = finish;
		vm.isAdmin = Principal.isAdmin;

		function canFinishOrder(order) {
			return vm.isAdmin() && !order.finished;
		}

		function finish(order) {
			Order.finish({id: order.id}).$promise
				.then(onOrderFinishingSuccess, onOrderFinishingFailed);

			function onOrderFinishingSuccess(data) {
				order.finished = true;
			}

			function onOrderFinishingFailed(response) {
				toastr.error("Error occurred while finishing the order! Please try again.",
					"Request Failed");
			}
		}
	}
})();
