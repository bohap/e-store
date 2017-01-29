(function() {
	'use strict';

	var orderDetails = {
		templateUrl: 'app/entities/order/order-details.html',
		controller: 'OrderDetailsController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('orderDetails', orderDetails);

	canRouteActivate.$inject = ['$nextInstruction', 'Principal', '$rootRouter', 'MessageUtil'];

	function canRouteActivate($nextInstruction, Principal, $rootRouter, MessageUtil) {
		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (identity === null) {
				MessageUtil.showNotAuthenticated();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
