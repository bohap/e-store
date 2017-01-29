(function() {
	'use strict';

	angular
		.module('app')
		.factory('BookUtil', BookUtil);

	function BookUtil() {
		var bookDescriptionMaxSize = 100;

		var service = {
			isBookDescriptionLong: isBookDescriptionLong,
			getBookDescriptionMaxSize: getBookDescriptionMaxSize,
			isBookOnPromotion: isBookOnPromotion,
			getBookPrice: getBookPrice,
			getBookDiscount: getBookDiscount
		};

		return service;

		function isBookDescriptionLong(book) {
			return angular.isDefined(book) && book !== null &&
				angular.isDefined(book.shortDescription) && book.shortDescription !== null &&
				book.shortDescription.length > bookDescriptionMaxSize;
		}

		function getBookDescriptionMaxSize() {
			return bookDescriptionMaxSize;
		}

		function isBookOnPromotion(book) {
			if (angular.isUndefined(book) || book === null) {
				return false;
			}

			var promotion = book.promotion;
			if (angular.isUndefined(promotion) || promotion === null) {
				return false;
			}

			var start = promotion.start;
			if (angular.isUndefined(start) || start === null) {
				return false;
			}

			return new Date(start).getTime() < new Date().getTime();
		}

		function getBookPrice(book) {
			if (isBookOnPromotion(book)) {
				return book.promotion.newPrice;
			}
			return book.price;
		}

		function getBookDiscount(book) {
			if (!isBookOnPromotion(book)) {
				return 0;
			}
			var currPrice = book.price;
			var newPrice = book.promotion.newPrice;
			return Math.floor((newPrice / currPrice) * 100);
		}
	}
})();
