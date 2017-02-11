(function() {
	'use strict';

	angular
		.module('app.order.partial')
		.controller('OrderListController', OrderListController);

	OrderListController.$inject = ['Principal', 'Order', 'ToastrNotify'];

	function OrderListController(Principal, Order, ToastrNotify) {
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
				ToastrNotify.error("Error occurred while finishing the order! Please try again.",
					"Request Failed");
			}
		}
	}
})();
