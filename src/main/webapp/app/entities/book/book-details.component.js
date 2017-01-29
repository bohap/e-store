(function() {
	'use strict';

	var bookDetails = {
		templateUrl: 'app/entities/book/book-details.html',
		controller: 'BookDetailsController',
		controllerAs: 'vm'
	};

	angular
		.module('app')
		.component('bookDetails', bookDetails);
})();
