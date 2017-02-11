(function() {
	'use strict';

	angular
		.module('app.order')
		.controller('OrderDetailsController', OrderDetailsController);

	OrderDetailsController.$inject = ['Order', 'ToastrNotify', '$rootRouter', 'EVENTS', '$scope'];

	function OrderDetailsController(Order, ToastrNotify, $rootRouter, EVENTS, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.order = {
			items: []
		};
		vm.totalPrice = 0;
		vm.isOrderLoaded = false;
		vm.activeTab = 0;

		function onRouterActivated(next) {
			var id = next.params.id;

			Order.get({id: id}).$promise
				.then(onOrderLoadingSuccess, onOrderLoadingFailed);

			function onOrderLoadingSuccess(order) {
				vm.order = order;

				Order.books({id: id}).$promise
					.then(onOrderBooksLoadingSuccess, onOrderLoadingFailed);

				function onOrderBooksLoadingSuccess(books) {
					vm.order.items = books;
					vm.isOrderLoaded = true;

					vm.order.items.forEach(function(item) {
						item.totalPrice = item.price * item.quantity;
					});
					vm.totalPrice = totalPrice();
				}
			}

			function onOrderLoadingFailed(response) {
				var status = response.status;
				if (status === 403 || status === 404) {
					$rootRouter.navigate(['Book']);
				} else {
					ToastrNotify.error("Error occurred while loading the page! Please reload it.",
						"Loading Failed");
				}
			}
		}

		function totalPrice() {
			var total = 0;
			vm.order.items.forEach(function(item) {
				total += item.price * item.quantity;
			});
			return total;
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Book']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
