(function() {
	'use strict';

	angular
		.module('app')
		.controller('DashboardUsersController', DashboardUsersController);

	DashboardUsersController.$inject = ['User', 'toastr', 'EVENTS', '$rootRouter', '$scope'];

	function DashboardUsersController(User, toastr, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.users = [];

		function onRouterActivated() {
			User.query({minified: false}).$promise
				.then(onUsersLoadingSuccess, onUsersLoadingFailed);

			function onUsersLoadingSuccess(data) {
				vm.users = data;
				vm.users.forEach(function(u) {
					var authoritiesString = "";
					u.authorities.forEach(function(a, index) {
						if (index === u.authorities.length - 1) {
							authoritiesString += a.name;
						} else {
							authoritiesString += a.name + ", ";
						}
					});
					u.authoritiesString = authoritiesString;
				});
			}

			function onUsersLoadingFailed(response) {
				toastr.error("Loading the users data failed. Please relaod the page", "Loading Failed");
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
