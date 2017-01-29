(function() {
	'use strict';

	var dashboardUsers = {
		templateUrl: 'app/entities/dashboard/dashboard-users.html',
		controller: 'DashboardUsersController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('dashboardUsers', dashboardUsers);

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
