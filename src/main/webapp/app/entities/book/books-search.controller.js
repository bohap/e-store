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
		vm.query = {
			value: null,
			invalid: false
		};
		vm.sending = false;
		vm.allBooksLoaded = false;

		function search() {
			if (vm.sending) {
				return;
			}
			if (vm.query.value === null || vm.query.value.length < 3) {
				vm.query.invalid = true;
				return;
			}
			vm.query.invalid = false;
			offset = 0;
			vm.allBooksLoaded = false;
			load();
		}

		function loadMore() {
			if (vm.query.value === null || vm.query.value.length < 3 || vm.sending || vm.allBooksLoaded) {
				return;
			}

			offset += limit;
			load();
		}

		function load() {
			vm.sending = true;
			Book.search({query: vm.query.value, limit: limit, offset: offset}).$promise
				.then(onBooksLoadingSuccess, onBooksLoadingFailed);

			function onBooksLoadingSuccess(books) {
				vm.sending = false;
				if (offset === 0) {
					vm.books = books;
				} else {
					vm.books = vm.books.concat(books);
				}
				if (books.length < limit) {
					vm.allBooksLoaded = true;
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
