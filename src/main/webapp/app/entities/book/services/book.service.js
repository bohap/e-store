(function() {
	'use strict';

	angular
		.module('app')
		.factory('Book', Book);

	Book.$inject = ['$resource'];

	function Book($resource) {
		var resourcePath = "/api/books/:slug/:path";
		return $resource(resourcePath, {slug: "@slug"}, {
			save: {
				method: "POST",
				headers: {'Content-Type': undefined },
				transformRequest: transformRequest
			},
			update: {
				method: 'PUT',
				headers: {'Content-Type': undefined },
				transformRequest: transformRequest
			},
			count: {
				method: 'GET',
				params: {
					path: 'count'
				}
			}
		});

		function transformRequest(data) {
			var formData = new FormData();
	        formData.append('bookVM', new Blob([angular.toJson(data.book)], {
	            type: "application/json"
	        }));
	        formData.append("image", data.image);
	        return formData;
		};
	}
})();
