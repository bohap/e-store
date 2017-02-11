(function() {
	'use strict';

	angular
		.module('app.category.service')
		.factory('Category', Category);

	Category.$inject = ['$resource'];

	function Category($resource) {
		var resourceUrl = "/api/categories";
		return $resource(resourceUrl, {});
	}
})();
