(function() {
	'use strict';

	angular
		.module('app.basket.service')
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
