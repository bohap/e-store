(function() {
	'use strict';

	var bookForm  = {
		templateUrl: 'app/entities/book/_partials/book-form.html',
		controller: 'BookFormController',
		controllerAs: 'vm',
		bindings: {
			book: '<',
			errors: '<',
			btnSaveText: '@',
			sending: '<',
			save: '&'
		}
	};

	angular
		.module('app')
		.component('bookForm', bookForm);
})();
