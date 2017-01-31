(function() {
	'use strict';

	var booksList = {
		templateUrl: 'app/entities/book/_partials/books-table-list.html',
		controller: 'BooksTableListController',
		controllerAs: 'vm',
		bindings: {
			books: '<'
		}
	};

	angular
		.module('app')
		.component('booksTableList', booksList);
})();
