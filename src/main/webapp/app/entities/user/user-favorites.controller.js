(function() {
	'use strict';

	angular
		.module('app.user')
		.controller('UserFavoritesController', UserFavoritesController);

	UserFavoritesController.$inject = ['User', 'ToastrNotify', '$rootRouter', 'EVENTS', '$scope'];

	function UserFavoritesController(User, ToastrNotify, $rootRouter, EVENTS, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.favorites = [];
		vm.loading = false;

		function onRouterActivated(next, prev) {
			vm.loading = true;
			var slug = next.params.slug;

			User.favorites({slug: slug}).$promise
				.then(onFavoritesLoadingSuccess, onFavoritesLoadingFailed);

			function onFavoritesLoadingSuccess(favorites) {
				vm.favorites = favorites;
				vm.loading = false;
			}

			function onFavoritesLoadingFailed(response) {
				vm.loading = false;
				var status = response.status;
				if (status === 404) {
					$rootRouter.navigate(['Home']);
				} else {
					ToastrNotify.error("Error occurred while laoding the page! Please reload it.",
						"Loading Failed");
				}
			}
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Home']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
