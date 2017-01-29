(function() {
	'use strict';

	var app = {
		templateUrl: 'app/root/app.html',
		$routeConfig: [
			{
				path: '/books/...',
				name: 'Book',
				component: 'book',
				useAsDefault: true
			},
			{
				path: '/users/...',
				name: 'User',
				component: 'user'
			},
			{
				path: '/dashboard/...',
				name: 'Dashboard',
				component: 'dashboard'
			},
			{
				path: '/basket',
				name: 'Basket',
				component: 'basket'
			},
			{
				path: '/orders/...',
				name: 'Order',
				component: 'order'
			}
		]
	};

	angular
		.module('app')
		.component('app', app);
})();
