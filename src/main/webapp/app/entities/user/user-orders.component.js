(function() {
	'use strict';

	var userOrders = {
		templateUrl: 'app/entities/user/user-orders.html',
		controller: 'UserOrdersController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.user')
		.component('userOrders', userOrders);

	canRouteActivate.$inject = ['$nextInstruction', 'Principal', '$rootRouter', 'ToastrNotify'];

	function canRouteActivate($nextInstruction, Principal, $rootRouter, ToastrNotify) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			var slug = $nextInstruction.params.slug;
			if (!Principal.isAdmin() && identity.slug !== slug) {
				ToastrNotify.showNotAuthorized();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
