(function() {
	'use strict';

	angular
		.module('app.basket.service')
		.factory('BasketBook', BasketBook);

	BasketBook.$inject = ['$resource'];

	function BasketBook($resource) {
		var resourcePath = "/api/basket/books/:slug/:path";
		return $resource(resourcePath, {slug: "@slug"}, {
			exists: {
				method: 'GET',
				params: {
					path: 'exists'
				}
			}
		});
	}
})();
