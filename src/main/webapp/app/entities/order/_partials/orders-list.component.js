(function() {
	'use strict';

	var ordersList = {
		templateUrl: 'app/entities/order/_partials/orders-list.html',
		controller: 'OrderListController',
		controllerAs: 'vm',
		bindings: {
			orders: '<'
		}
	};

	angular
		.module('app.order.partial')
		.component('ordersList', ordersList);
})();
