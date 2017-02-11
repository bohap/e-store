(function() {
	angular
		.module('app.auth')
		.factory('Login', Login);

	Login.$inject = ['$http'];

	function Login($http) {
		var resourceUrl = "/api/auth/authenticate";
		var service = {
			login: login
		};

		return service;

		function login(credentials) {
			return $http.post(resourceUrl, credentials);
		}
	}
})();
