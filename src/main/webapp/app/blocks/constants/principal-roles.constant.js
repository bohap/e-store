(function() {
	'use strict';

	var roles = {
		admin: "admin",
		regular: "user"
	};

	angular
		.module('app')
		.constant('ROLES', roles);
})();
