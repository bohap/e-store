(function() {
	'use strict';

	angular
		.module('app')
		.controller('UserFavoritesController', UserFavoritesController);

	UserFavoritesController.$inject = ['User', 'toastr', '$rootRouter', 'EVENTS', '$scope'];

	function UserFavoritesController(User, toastr, $rootRouter, EVENTS, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.favorites = [];
		vm.areFavoritesLoaded = false;

		function onRouterActivated(next, prev) {
			var slug = next.params.slug;

			User.favorites({slug: slug}).$promise
				.then(onFavoritesLoadingSuccess, onFavoritesLoadingFailed);

			function onFavoritesLoadingSuccess(favorites) {
				vm.favorites = favorites;
				vm.areFavoritesLoaded = true;
			}

			function onFavoritesLoadingFailed(response) {
				var status = response.status;
				if (status === 404) {
					$rootRouter.navigate(['Book']);
				} else {
					toastr.error("Error occurred while laoding the page! Please reload it.",
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
