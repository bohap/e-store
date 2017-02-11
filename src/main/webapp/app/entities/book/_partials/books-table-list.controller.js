(function() {
	'use strict';

	angular
		.module('app.book.partial')
		.controller('BooksTableListController', BooksTableListController);

	BooksTableListController.$inject = ['ConfirmDialog', 'PromotionDialog', 'ToastrNotify', 'Promotion', 'Principal', 'Book'];

	function BooksTableListController(ConfirmDialog, PromotionDialog, ToastrNotify, Promotion, Principal, Book) {
		var vm = this;

		vm.isEmpty = isEmpty;
		vm.isAdmin = Principal.isAdmin;
		vm.hasPromotion = hasPromotion;
		vm.isPromotionStarted = isPromotionStarted;
		vm.getPromotionPrice = getPromotionPrice;
		vm.deleteBook = deleteBook;
		vm.openPromotionDialog = openPromotionDialog;
		vm.removePromotion = removePromotion;

		function isEmpty() {
			return vm.books.length === 0;
		}

		function hasPromotion(book) {
			return angular.isDefined(book) && book !== null &&
				angular.isDefined(book.promotion) && book.promotion !== null;
		}

		function isPromotionStarted(book) {
			if (!hasPromotion(book)) {
				return false;
			}
			return new Date(book.promotion.start).getTime() < new Date().getTime();
		}

		function getPromotionPrice(book) {
			if (!hasPromotion(book)) {
				return undefined;
			}
			return book.promotion.newPrice;
		}

		function deleteBook(book) {
			var message = "Are you sure you want to delete book " + book.name + "?";
			ConfirmDialog.open(message)
				.then(onDialogConfirmed, onDialogCanceled);

			function onDialogConfirmed() {
				Book.remove({slug: book.slug}).$promise
					.then(onBookRemovingSuccess, onBookRemovingFailed);

				function onBookRemovingSuccess() {
					var index = vm.books.indexOf(book);
					vm.books.splice(index, 1);
				}

				function onBookRemovingFailed() {
					ToastrNotify.error("Removing the book failed! Please try again.", "Remove Failed");
				}
			}

			function onDialogCanceled() {
			}
		}

		function openPromotionDialog(book) {
			PromotionDialog.open(book);
		}

		function removePromotion(book) {
			if (book.isPromotionRemoving) {
				return;
			}

			book.isPromotionRemoving = true;
			Promotion.remove({slug: book.slug}).$promise
				.then(onPromotionRemovingSuccess, onPromotionRemovingFailed);

			function onPromotionRemovingSuccess(data) {
				book.isPromotionRemoving = false;
				book.promotion = null;
			}

			function onPromotionRemovingFailed(response) {
				book.isPromotionRemoving = false;
				ToastrNotify.error("Removing the book promotion failed!. Please try again.", "Removing Failed");
			}
		}
	}
})();
