(function() {
	'use strict';

	angular
		.module('app.promotion.service')
		.factory('Promotion', Promotion);

	Promotion.$inject = ['$resource'];

	function Promotion($resource) {
		var resourcePath = "/api/books/:slug/promotion";
		return $resource(resourcePath, {slug: "@slug"});
	}
})();
