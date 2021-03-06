(function() {
	'use strict';

	var events = {
		authenticationSuccess: 'auth:login-success',
		logoutSuccess: 'auth:logout-success',
		bookImageSelected: 'book:image-selected'
	};

	angular
		.module('app.constants')
		.constant('EVENTS', events);
})();
