(function() {
	'use strict';

	angular
		.module('app')
		.factory('Category', Category);

	Category.$inject = ['$resource'];

	function Category($resource) {
		var resourceUrl = "/api/categories";
		return $resource(resourceUrl, {});
	}
})();
