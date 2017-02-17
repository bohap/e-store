'use strict';
describe("BookUtil Service Test", function() {
	var BookUtil;

	beforeEach(angular.mock.module('app.book.service'));

	beforeEach(inject(function(_BookUtil_) {
		BookUtil = _BookUtil_;
	}));

	describe("is book description long", function() {
		it("should return false when the book or the book description is undefined on null", function() {
			expect(BookUtil.isBookDescriptionLong()).toBeFalsy();
			expect(BookUtil.isBookDescriptionLong(null)).toBeFalsy();
			var book = {};
			expect(BookUtil.isBookDescriptionLong(book)).toBeFalsy();
			book.shortDescription = null;
			expect(BookUtil.isBookDescriptionLong(book)).toBeFalsy();
		});

		it("should return true when the book description is bigger then the limit", function() {
			var max = BookUtil.getBookDescriptionMaxSize();
			var book = {};
			var description = "";
			for (var i = 0; i < max + 1; i++) {
				description += "a";
			}
			book.shortDescription = description;

			expect(BookUtil.isBookDescriptionLong(book)).toBeTruthy();
		});

		it("should return false when the book description is lesser or equal then the limit", function() {
			var max = BookUtil.getBookDescriptionMaxSize();
			var book = {};
			var description = "";
			for (var i = 0; i < max - 1; i++) {
				description += "a";
			}
			book.shortDescription = description;
			expect(BookUtil.isBookDescriptionLong(book)).toBeFalsy();
			book.shortDescription += "a";
			expect(BookUtil.isBookDescriptionLong(book)).toBeFalsy();
		});
	});

	describe("is book on promotion", function() {
		it("should return false for undefined or null book or book promotion or promotion start date", function() {
			expect(BookUtil.isBookOnPromotion()).toBeFalsy();
			expect(BookUtil.isBookOnPromotion(null)).toBeFalsy();

			var book = {};
			expect(BookUtil.isBookOnPromotion(book)).toBeFalsy();
			book.promotion = null;
			expect(BookUtil.isBookOnPromotion(book)).toBeFalsy();

			book.promotion = {};
			expect(BookUtil.isBookOnPromotion(book)).toBeFalsy();
			book.promotion.start = null;
			expect(BookUtil.isBookOnPromotion(book)).toBeFalsy();
		});

		it("should return true when the promotion start date is before now", function() {
			var now = new Date().getTime();
			var start = new Date(now - (24 * 60 * 60 * 1000));
			var book = { promotion: { start: start } };
			expect(BookUtil.isBookOnPromotion(book)).toBeTruthy();
		});

		it("should return false when the promotion start date is after now", function() {
			var now = new Date().getTime();
			var start = new Date(now + (24 * 60 * 60 * 1000));
			var book = { promotion: { start: start } };
			expect(BookUtil.isBookOnPromotion(book)).toBeFalsy();
		});
	});

	describe("get book price", function() {
		it("should return 0 for null of undefined book", function() {
			expect(BookUtil.getBookPrice()).toEqual(0);
			expect(BookUtil.getBookPrice(null)).toEqual(0);
		});

		it("should return the promotion price when the book is on promotion", function() {
			var now = new Date().getTime();
			var start = new Date(now - (24 * 60 * 60 * 1000));
			var book = {
				promotion: {
					newPrice: 5345,
					start: start
				},
				price: 213412
			};
			expect(BookUtil.getBookPrice(book)).toEqual(book.promotion.newPrice);
		});

		it("should return the book price when the book is not on promotion", function() {
			var now = new Date().getTime();
			var start = new Date(now + (24 * 60 * 60 * 1000));
			var book = {
				promotion: {
					newPrice: 5345,
					start: start
				},
				price: 213412
			};
			expect(BookUtil.getBookPrice(book)).toEqual(book.price);
		});
	});

	describe("get book discount", function() {
		it("should return 0 for null or undefined book", function() {
			expect(BookUtil.getBookDiscount()).toEqual(0);
			expect(BookUtil.getBookDiscount(null)).toEqual(0);
		});

		it("should return 0 when the book is not on promotion", function() {
			var now = new Date().getTime();
			var start = new Date(now + (24 * 60 * 60 * 1000));
			var book = { promotion: { start: start } };
			expect(BookUtil.getBookDiscount(book)).toEqual(0);
		});

		it("should return the discount when the book is on promotion", function() {
			var now = new Date().getTime();
			var start = new Date(now - (24 * 60 * 60 * 1000));
			var book = {
				promotion: {
					newPrice: 89,
					start: start
				},
				price: 500
			};
			var discount = 82;

			expect(BookUtil.getBookDiscount(book)).toEqual(discount);
		});
	});
});