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
		.module('app.book')
		.component('bookUpdate', bookUpdate);

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
