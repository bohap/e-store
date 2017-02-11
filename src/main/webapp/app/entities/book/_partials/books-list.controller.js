(function() {
	'use strict';

	angular
		.module('app.book.partial')
		.controller('BooksListController', BooksListController);

	BooksListController.$inject = ['BookUtil'];

	function BooksListController(BookUtil) {
		var vm = this;

		vm.isBookDescriptionLong = BookUtil.isBookDescriptionLong;
		vm.isBookOnPromotion = BookUtil.isBookOnPromotion;
		vm.getBookDiscount = BookUtil.getBookDiscount;
		vm.getBookPrice = BookUtil.getBookPrice;
		vm.bookDescriptionMaxSize = BookUtil.getBookDescriptionMaxSize();
	}
})();
