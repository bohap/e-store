(function() {
	'use strict';

	var basket = {
		templateUrl: 'app/entities/basket/basket.html',
		controller: 'BasketController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('basket', basket);

	canRouteActivate.$inject = ['Principal', '$rootRouter', 'MessageUtil', 'toastr'];

	function canRouteActivate(Principal, $rootRouter, MessageUtil, toastr) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (identity === null) {
				MessageUtil.showNotAuthenticated();
				$rootRouter.navigate(['Book']);
				return false;
			}
			if (Principal.isAdmin()) {
				toastr.error("Admin users can't have a basket.", "Not Allowed");
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
