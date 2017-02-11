(function() {
	'use strict';

	var errors = {
		templateUrl: 'app/layouts/error/errors-list.html',
		controller: 'ErrorsListController',
		controllerAs: 'vm',
		bindings: {
			errors: '<'
		}
	};

	angular
		.module('app.error')
		.component('errorsList', errors);
})();
