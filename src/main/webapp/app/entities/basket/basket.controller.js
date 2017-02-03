(function() {
	'use strict';

	angular
		.module('app')
		.controller("BasketController", BasketController);

	BasketController.$inject = ['BasketBook', 'BookUtil', 'toastr', 'OrderCreateDialog',
		'EVENTS', '$rootRouter', '$scope'];

	function BasketController(BasketBook, BookUtil, toastr, OrderCreateDialog, EVENTS,
								$rootRouter, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.isEmpty = isEmpty;
		vm.getBookPrice = getBookPrice;
		vm.hasInvalidQuantity = hasInvalidQuantity;
		vm.totalPrice = totalPrice;
		vm.removeBook = removeBook;
		vm.openCreateOrderDialog = openCreateOrderDialog;
		vm.basket = {};
		vm.items = [];
		vm.initDataLoading = false;

		function onRouterActivated() {
			vm.initDataLoading = true;
			BasketBook.query().$promise
				.then(onBasketBooksLoadingSuccess, onBasketBooksLoadingFailed);

			function onBasketBooksLoadingSuccess(books) {
				vm.initDataLoading = false;
				vm.items = [];
				books.forEach(function(book) {
					var item = {
						book: book,
						quantity: 1
					};
					vm.items.push(item);
				});
			}

			function onBasketBooksLoadingFailed(response) {
				vm.initDataLoading = false;
				toastr.error("Some error occurred while loading the page! Please reload it.", "Loading Failed");
			}
		}

		function isEmpty() {
			return vm.items.length === 0;
		}

		function getBookPrice(item) {
			return BookUtil.getBookPrice(item.book);
		}

		function hasInvalidQuantity(item) {
			return item.quantity < 1;
		}

		function totalPrice() {
			var total = 0;
			vm.items.forEach(function(item) {
				var price = getBookPrice(item);
				total += price * item.quantity;
			});
			return total;
		}

		function removeBook(item) {
			BasketBook.remove({slug: item.book.slug}).$promise
				.then(onBookRemovingSuccess, onBookRemovingFailed);

			function onBookRemovingSuccess() {
				toastr.success("Book is successfullt removed from the basket", "Book Removed");
				var index = vm.items.indexOf(item);
				vm.items.splice(index, 1);
			}

			function onBookRemovingFailed() {
				toastr.error("Book removing failed! Please try again.", "Remove Failed");
			}
		}

		function openCreateOrderDialog() {
			if (!checkForInvalidQuantity()) {
				OrderCreateDialog.open(vm.items)
					.then(onOrderCreated, onOrderCreationCanceled);
			}

			function onOrderCreated() {
				vm.items = [];
			}

			function onOrderCreationCanceled() {
			}
		}

		function checkForInvalidQuantity() {
			for (var i = 0; i < vm.items.length; i++) {
				if (hasInvalidQuantity(vm.items[i])) {
					return true;
				}
			}
			return false;
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Book']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
