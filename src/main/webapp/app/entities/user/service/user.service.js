(function() {
	'use strict';

	angular
		.module('app.user.service')
		.factory('User', User);

	User.$inject = ['$resource'];

	function User($resource) {
		var resourcePath = "/api/users/:slug/:path";
		return $resource(resourcePath, {slug: "@slug"}, {
			count: {
				method: "GET",
				params: {
					path: "count"
				}
			},
			orders: {
				method: "GET",
				isArray: true,
				params: {
					path: "orders"
				}
			},
			favorites: {
				method: "GET",
				isArray: true,
				params: {
					path: "favorites"
				}
			}
		});
	}
})();
