(function() {
	'use strict';

	angular
		.module('app')
		.controller('UserOrdersController', UserOrdersController);

	UserOrdersController.$inject = ['User', 'toastr', '$rootRouter', 'EVENTS', '$scope'];

	function UserOrdersController(User, toastr, $rootRouter, EVENTS, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.orders = [];
		vm.areOrdersLoaded = false;

		function onRouterActivated(next, prev) {
			var slug = next.params.slug;

			User.orders({slug: slug}).$promise
				.then(onOrdersLoadingSuccess, onOrdersLoadingFailed);

			function onOrdersLoadingSuccess(orders) {
				vm.orders = orders;
				vm.areOrdersLoaded = true;
			}

			function onOrdersLoadingFailed(response) {
				var status = response.status;
				if (status === 404) {
					$rootRouter.navigate(['Book']);
				} else {
					toastr.error("Error occurred while loading the page! Please reload it.",
						"Loading Failed");
				}
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
