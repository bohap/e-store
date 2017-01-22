(function() {
	'use strict';

	var events = {
		authenticationSuccess: 'auth:login-success',
		logoutSuccess: 'auth:logout-success',
	};

	angular.module('app')
		.constant('EVENTS', events);
})();
