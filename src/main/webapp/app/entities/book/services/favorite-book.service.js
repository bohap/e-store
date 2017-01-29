(function() {
	'use strict';

	angular
		.module('app')
		.factory('FavoriteBook', FavoriteBook);

	FavoriteBook.$inject = ['$resource'];

	function FavoriteBook($resource) {
		var resourcePath = "/api/books/:slug/favorites/:path";
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
