(function() {
	'use strict';

	var user = {
		template: '<ng-outlet></ng-outlet>',
		$routeConfig: [
			{
				path: '/:slug/orders',
				name: 'UserOrders',
				component: 'userOrders'
			},
			{
				path: '/:slug/favoritess',
				name: 'UserFavorites',
				component: 'userFavorites'
			}
		]
	};

	angular
		.module('app.user')
		.component('user', user);
})();
