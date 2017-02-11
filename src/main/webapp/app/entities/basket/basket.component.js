(function() {
	'use strict';

	var basket = {
		templateUrl: 'app/entities/basket/basket.html',
		controller: 'BasketController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.basket')
		.component('basket', basket);

	canRouteActivate.$inject = ['Principal', '$rootRouter', 'ToastrNotify'];

	function canRouteActivate(Principal, $rootRouter, ToastrNotify) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (identity === null) {
				ToastrNotify.showNotAuthenticated();
				$rootRouter.navigate(['Book']);
				return false;
			}
			if (Principal.isAdmin()) {
				ToastrNotify.error("Admin users can't have a basket.", "Not Allowed");
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
