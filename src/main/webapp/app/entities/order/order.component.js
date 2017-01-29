(function() {
	'use strict';

	var order = {
		template: '<ng-outlet></ng-outlet?',
		$routeConfig: [
			{
				path: '/:id',
				name: 'OrderDetails',
				component: 'orderDetails'
			}
		]
	};

	angular
		.module('app')
		.component('order', order);
})();
