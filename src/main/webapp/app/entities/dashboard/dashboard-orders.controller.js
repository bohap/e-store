(function() {
	'use strict';

	angular
		.module('app.dashboard')
		.controller('DashboardOrderController', DashboardOrderController);

	DashboardOrderController.$inject = ['Order', 'ToastrNotify', 'EVENTS', "$rootRouter", '$scope'];

	function DashboardOrderController(Order, ToastrNotify, EVENTS, $rootRouter, $scope) {
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
				ToastrNotify.error("Error occurred while loading the page! Please reload it.", "Loading Failed");
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
