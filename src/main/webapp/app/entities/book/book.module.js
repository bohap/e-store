(function() {
	'use strict';

	angular.module('app.book', [
		'ngComponentRouter',
		'infinite-scroll',
		'ui.bootstrap.tooltip',
		'ui.select',
		'ngMessages',
		'app.constants',
		'app.util',
		'app.validate',
		'app.html',
		'app.key-event',
		'app.dialog',
		'app.error',
		'app.notify',
		'app.auth',
		'app.book.service',
		'app.book.partial',
		'app.book.directive',
		'app.category.service',
		'app.favorite.service',
		'app.basket.service',
	]);
})();
