(function() {
	'use strict';

	var dashboardHome = {
		templateUrl: 'app/entities/dashboard/dashboard-home.html',
		controller: 'DashboardHomeController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('dashboardHome', dashboardHome);

	canRouteActivate.$inject = ['Principal', '$rootRouter', 'MessageUtil'];

	function canRouteActivate(Principal, $rootRouter, MessageUtil) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (!Principal.isAdmin()) {
				MessageUtil.showNotAuthorized();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
