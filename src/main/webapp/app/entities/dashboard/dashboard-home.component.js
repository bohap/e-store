(function() {
	'use strict';

	var dashboardHome = {
		templateUrl: 'app/entities/dashboard/dashboard-home.html',
		controller: 'DashboardHomeController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.dashboard')
		.component('dashboardHome', dashboardHome);

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
