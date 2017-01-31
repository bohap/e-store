(function() {
	'use strict';

	angular
		.module('app')
		.controller('BooksIndexController', BooksIndexController);

	BooksIndexController.$inject = ['Book', 'Category'];

	function BooksIndexController(Book, Category) {
		var vm = this;
		var limit = 15;
		var offset = 0;

		vm.$routerOnActivate = onRouterActivated;
		vm.loadMore = loadMore;
		vm.filter = filter;
		vm.books = [];
		vm.categories = [];
		vm.selectedCategories = [];
		vm.loading = false;

		function onRouterActivated(next, prev) {
			// Load the books
			loadUnfiltered();

			// Load the categories
			Category.query().$promise
				.then(onCategoriesLoadingSuccess, onCategoriesLoadingFailed);

			function onCategoriesLoadingSuccess(data) {
				vm.categories = data;
			}

			function onCategoriesLoadingFailed(response) {
				vm.categories = [];
			}
		}

		function loadUnfiltered() {
			vm.loading = true;
			Book.query({limit: limit, offset: offset,  latest: true}).$promise
				.then(onBooksLoadingSuccess, onBooksLoadingFailed);
		}

		function onBooksLoadingSuccess(data) {
			vm.loading = false;
			if (offset === 0) {
				vm.books = data;
			} else {
				vm.books = vm.books.concat(data);
			}
		}

		function onBooksLoadingFailed(response) {
			vm.loading = false;
		}

		function loadMore() {
			if (vm.loading) {
				return;
			}
			offset += limit;
			if (vm.selectedCategories.length === 0) {
				loadUnfiltered();
			} else {
				loadFiltered();
			}
		}

		function filter(category) {
			if (vm.loading) {
				return;
			}
			offset = 0;
			if (category.selected) {
				category.selected = false;
				var index = vm.selectedCategories.indexOf(category);
				vm.selectedCategories.splice(index, 1);
			} else {
				category.selected = true;
				vm.selectedCategories.push(category);
			}

			if (vm.selectedCategories.length === 0) {
				loadUnfiltered();
			} else {
				loadFiltered();
			}
		}

		function loadFiltered() {
			vm.loading = true;
			var categoriesQuery = createCategoriesQuery();
			Book.query({limit: limit, offset: offset, latest: true, categories: categoriesQuery})
				.$promise.then(onBooksLoadingSuccess, onBooksLoadingFailed);
		}

		function createCategoriesQuery() {
			var result = "";
			vm.selectedCategories.forEach(function(c, index) {
				if (index === vm.selectedCategories.length - 1) {
					result += c.name;
				} else {
					result += c.name + ",";
				}
			});
			return result;
		}
	}
})();
