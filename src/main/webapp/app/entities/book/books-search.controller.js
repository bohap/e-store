(function() {
	'use strict';

	angular
		.module('app')
		.controller('BooksSearchController', BooksSearchController);

	BooksSearchController.$inject = ['Book', 'toastr'];

	function BooksSearchController(Book, toastr) {
		var vm = this,
			limit = 20,
			offset = 0;

		vm.search = search;
		vm.loadMore = loadMore;
		vm.books = [];
		vm.query = null;
		vm.invalid = false;
		vm.sending = false;

		function search() {
			if (vm.sending) {
				return;
			}
			if (vm.query === null || vm.query.length < 3) {
				vm.invalid = true;
			}
			vm.invalid = false;
			offset = 0;
			load();
		}

		function loadMore() {
			if (vm.query === null || vm.query.length < 3 || vm.sending) {
				return;
			}

			offset += limit;
			load();
		}

		function load() {
			vm.sending = true;
			Book.search({query: vm.query, limit: limit, offset: offset}).$promise
				.then(onBooksLoadingSuccess, onBooksLoadingFailed);

			function onBooksLoadingSuccess(books) {
				vm.sending = false;
				if (offset === 0) {
					vm.books = books;
				} else {
					vm.books = vm.books.concat(books);
				}
			}

			function onBooksLoadingFailed(response) {
				vm.sending = false;
				toastr.error("Error occurred while searching the books. Please try again.",
					"Loading Failed");
			}
		}
	}
})();
