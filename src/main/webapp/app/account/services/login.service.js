(function() {
	angular
		.module('app')
		.factory('Login', Login);

	Login.$inject = ['$http'];

	function Login($http) {
		var resourceUrl = "/api/auth/login";
		var service = {
			login: login
		};

		return service;

		function login(credentials) {
			return $http.post(resourceUrl, credentials);
		}
	}
})();
