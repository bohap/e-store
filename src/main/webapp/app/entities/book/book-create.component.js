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
		.module('app.book')
		.component('bookCreate', bookCreate);

	canRouteActivate.$inject = ['Principal', '$rootRouter', 'ToastrNotify'];

	function canRouteActivate(Principal, $rootRouter, ToastrNotify) {

		return Principal.resolveIdentity()
			.then(onIdentityResolved);

		function onIdentityResolved(identity) {
			if (!Principal.isAdmin()) {
				ToastrNotify.showNotAuthorized();
				$rootRouter.navigate(['Book']);
				return false;
			}
			return true;
		}
	}
})();
