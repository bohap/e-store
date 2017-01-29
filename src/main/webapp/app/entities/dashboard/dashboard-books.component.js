(function() {
	'use strict';

	var dashboardBooks = {
		templateUrl: 'app/entities/dashboard/dashboard-books.html',
		controller: 'DasboardBooksController',
		controllerAs: 'vm',
		bindings: {
			$router: '<'
		},
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('dashboardBooks', dashboardBooks);

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
