(function() {
	'use strict';

	angular.module('app.basket', [
		'ngComponentRouter',
		'ui.bootstrap.tooltip',
		'ui.bootstrap.popover',
		'app.constants',
		'app.auth',
		'app.notify',
		'app.basket.service',
		'app.book.service',
		'app.order.dialog'
	]);
})();
