(function() {
	'use strict';

	var orderDetails = {
		templateUrl: 'app/entities/order/order-details.html',
		controller: 'OrderDetailsController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.order')
		.component('orderDetails', orderDetails);

	canRouteActivate.$inject = ['$nextInstruction', 'Principal', '$rootRouter', 'ToastrNotify'];

	function canRouteActivate($nextInstruction, Principal, $rootRouter, ToastrNotify) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (identity === null) {
				ToastrNotify.showNotAuthenticated();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
