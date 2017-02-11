(function() {
	'use strict';

	angular.module('app.book.partial', [
		'ngComponentRouter',
		'ui.bootstrap.tooltip',
		'smart-table',
		'angularMoment',
		'app.constants',
		'app.notify',
		'app.dialog',
		'app.auth',
		'app.book.service',
		'app.promotion.service',
		'app.promotion.dialog'
	]);
})();
