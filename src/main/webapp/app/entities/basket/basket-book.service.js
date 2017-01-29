(function() {
	'use strict';

	angular
		.module('app')
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
