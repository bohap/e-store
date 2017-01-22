(function() {
	'use strict';

	angular
		.module('app')
		.factory('Account', Account);

	Account.$inject = ['$resource'];

	function Account($resource) {
		var resourceUrl = '/api/auth/account';
		return $resource(resourceUrl, {});
	}
})();
