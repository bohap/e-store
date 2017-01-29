(function() {
	'use strict';

	var bookUpdate = {
		templateUrl: 'app/entities/book/book-update.html',
		controller: 'BookUpdateController',
		controllerAs: 'vm',
		bindings: {
			$router: '<'
		},
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('bookUpdate', bookUpdate);

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
