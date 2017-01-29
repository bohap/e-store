(function() {
	'use strict';

	angular
		.module('app')
		.controller('DashboardHomeController', DashboardHomeController);

	DashboardHomeController.$inject = ['Book', 'User', 'Order', 'toastr', 'EVENTS', '$rootRouter', '$scope'];

	function DashboardHomeController(Book, User, Order, toastr, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.booksCount = 0;
		vm.usersCount = 0;
		vm.ordersCount = 0;

		function onRouterActivated() {
			Book.count().$promise
				.then(onBooksCountLoadingSuccess, onDataLoadingFailed);

			function onBooksCountLoadingSuccess(data) {
				vm.booksCount = data.count;
			}

			User.count().$promise
				.then(onUsersCountLoadingSuccess, onDataLoadingFailed);

			function onUsersCountLoadingSuccess(data) {
				vm.usersCount = data.count;
			}

			Order.count().$promise
				.then(onOrdersLoadingSuccess, onDataLoadingFailed);

			function onOrdersLoadingSuccess(data) {
				vm.ordersCount = data.count;
			}

			function onDataLoadingFailed(response) {
				toastr.error("Error occurred while loading the page! Please try to reload it.",
					"Loading Failed");
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
