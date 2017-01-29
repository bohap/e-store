(function() {
	'use strict';

	angular
		.module('app')
		.controller('DashboardOrderController', DashboardOrderController);

	DashboardOrderController.$inject = ['Order', 'toastr', 'EVENTS', "$rootRouter", '$scope'];

	function DashboardOrderController(Order, toastr, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.orders = [];

		function onRouterActivated() {
			Order.query().$promise
				.then(onOrdersLoadingSuccess, onOrdersLoadingFailed);

			function onOrdersLoadingSuccess(orders) {
				vm.orders = orders;
			}

			function onOrdersLoadingFailed(response) {
				toastr.error("Error occurred while loading the page! Please reload it.", "Loading Failed");
			}
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Book']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
