(function() {
	'use strict';

	angular
		.module('app.auth')
		.factory('Register', Register);

	Register.$inject = ['$http'];

	function Register($http) {
		var resourceUrl = "/api/auth/register";
		var service = {
			register: register
		};

		return service;

		function register(account) {
			return $http.post(resourceUrl, account);
		}
	}
})();
