(function() {
	'use strict';

	var dashboardOrders = {
		templateUrl: 'app/entities/dashboard/dashboard-orders.html',
		controller: 'DashboardOrderController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.dashboard')
		.component('dashboardOrders', dashboardOrders);

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
