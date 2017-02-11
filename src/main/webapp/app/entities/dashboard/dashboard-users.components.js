(function() {
	'use strict';

	var dashboardUsers = {
		templateUrl: 'app/entities/dashboard/dashboard-users.html',
		controller: 'DashboardUsersController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.dashboard')
		.component('dashboardUsers', dashboardUsers);

	canRouteActivate.$inject = ['Principal', '$rootRouter', 'ToastrNotify'];

	function canRouteActivate(Principal, $rootRouter, ToastrNotify) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (!Principal.isAdmin()) {
				ToastrNotify.showNotAuthorized();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
