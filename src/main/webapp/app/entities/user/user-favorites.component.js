(function() {
	'use strict';

	var userFavorites = {
		templateUrl: 'app/entities/user/user-favorites.html',
		controller: 'UserFavoritesController',
		controllerAs: 'vm',
		$canActivate: canRouteActivate
	};

	angular
		.module('app.user')
		.component('userFavorites', userFavorites);

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
