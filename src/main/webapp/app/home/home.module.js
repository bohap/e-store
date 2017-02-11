(function() {
	'use strict';

	angular.module('app.home', [
		'ngComponentRouter',
		'ui.bootstrap.carousel',
		'app.notify',
		'app.book.partial',
		'app.book.service'
	]);
})();
