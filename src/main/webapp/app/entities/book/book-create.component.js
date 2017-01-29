(function() {
	'use strict';

	var bookCreate = {
		templateUrl: 'app/entities/book/book-create.html',
		controller: 'BookCreateController',
		controllerAs: 'vm',
		bindings: {
			$router: '<'
		},
		$canActivate: canRouteActivate
	};

	angular
		.module('app')
		.component('bookCreate', bookCreate);

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
