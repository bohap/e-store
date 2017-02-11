(function() {
	'use strict';

	var booksList = {
		templateUrl: 'app/entities/book/_partials/books-list.html',
		controller: 'BooksListController',
		controllerAs: 'vm',
		bindings: {
			books: '<'
		}
	};

	angular
		.module('app.book.partial')
		.component('booksList', booksList);
})();
