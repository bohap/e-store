(function() {
	'use strict';

	var dashboard = {
		template: '<ng-outlet> </ng-outlet>',
		$routeConfig: [
			{
				path: '/',
				name: 'DashboardHome',
				component: 'dashboardHome',
				useAsDefault: true
			},
			{
				path: '/books',
				name: 'DashboardBooks',
				component: 'dashboardBooks'
			},
			{
				path: '/users',
				name: 'DashboardUsers',
				component: 'dashboardUsers'
			},
			{
				path: '/orders',
				name: 'DashboardOrders',
				component: 'dashboardOrders'
			}
		]
	};

	angular
		.module('app')
		.component('dashboard', dashboard);
})();
