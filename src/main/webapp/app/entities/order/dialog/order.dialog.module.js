(function() {
	'use strict';

	angular.module('app.order.dialog', [
		'ui.bootstrap.modal',
		'ui.bootstrap.tooltip',
		'ui.bootstrap.tabs',
		'app.notify',
		'app.validate',
		'app.html',
		'app.book.service',
		'app.basket.service'
	]);
})();
