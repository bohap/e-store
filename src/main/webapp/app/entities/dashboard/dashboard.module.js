(function() {
	'use strict';

	angular.module('app.dashboard', [
		'ngComponentRouter',
		'ui.bootstrap.tooltip',
		'smart-table',
		'app.constants',
		'app.auth',
		'app.notify',
		'app.book.service',
		'app.book.partial',
		'app.user.service',
		'app.order.service',
		'app.order.partial'
	]);
})();
