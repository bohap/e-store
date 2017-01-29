(function() {
	'use strict';

	var dashboardOrders = {
		templateUrl: 'app/entities/dashboard/dashboard-orders.html',
		controller: 'DashboardOrderController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('dashboardOrders', dashboardOrders);

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
