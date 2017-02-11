(function() {
	'use strict';

	var bookForm  = {
		templateUrl: 'app/entities/book/book-form.html',
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
		.module('app.book')
		.component('bookForm', bookForm);
})();
