(function() {
	'use strict';

	angular
		.module('app')
		.factory('Basket', Basket);

	Basket.$inject = ['$resource'];

	function Basket($resource) {
		var resourcePath = "/api/basket/checkout";
		return $resource(resourcePath, {}, {
			checkout: {
				method: 'POST'
			}
		});
	}
})();
