(function() {
	'use strict';

	var booksIndex = {
		templateUrl: 'app/entities/book/books-index.html',
		controller: 'BooksIndexController',
		controllerAs: 'vm'
	};

	angular
		.module('app.book')
		.component('booksIndex', booksIndex);
})();
