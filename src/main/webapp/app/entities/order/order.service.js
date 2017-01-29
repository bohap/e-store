(function() {
	'use strict';

	angular
		.module('app')
		.factory('Order', Order);

	Order.$inject = ['$resource'];

	function Order($resource) {
		var resourcePath = "/api/orders/:id/:path";
		return $resource(resourcePath, {id: "@id"}, {
			count: {
				method: "GET",
				params: {
					path: "count"
				}
			},
			finish: {
				method: "PUT",
				params: {
					path: "finish"
				}
			},
			books: {
				method: "GET",
				isArray: true,
				params: {
					path: "books"
				}
			}
		});
	}
})();
