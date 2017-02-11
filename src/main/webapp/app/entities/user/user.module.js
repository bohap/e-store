(function() {
	'use strict';

	angular.module('app.user', [
		'ngComponentRouter',
		'app.constants',
		'app.auth',
		'app.notify',
		'app.user.service',
		'app.book.partial',
		'app.order.partial'
	]);
})();
