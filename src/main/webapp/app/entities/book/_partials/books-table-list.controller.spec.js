'use strict';
describe("BooksTableList Controller test", function() {
	var BooksTableListController, ConfirmDialog, PromotionDialog, ToastrNotify, Promotion, Principal, Book, $q, $rootScope;
	var books = [
		{ name: "Book 1", slug: "book-1", price: 123 },
		{ name: "Book 2", slug: "book-2", price: 423 },
		{ name: "Book 3", slug: "book-3", price: 7846 },
		{ name: "Book 4", slug: "book-4", price: 75893 },
		{ name: "Book 5", slug: "book-5", price: 4823 }
	];
	var size = books.length;

	beforeEach(angular.mock.module('app.book.partial'));

	beforeEach(inject(function($controller, _ConfirmDialog_, _PromotionDialog_, _ToastrNotify_,
								 _Promotion_, _Principal_, _Book_, _$q_, _$rootScope_) {
		ConfirmDialog = _ConfirmDialog_;
		PromotionDialog = _PromotionDialog_;
		ToastrNotify = _ToastrNotify_;
		Promotion = _Promotion_;
		Principal = _Principal_;
		Book = _Book_;
		$q = _$q_;
		$rootScope = _$rootScope_;

		BooksTableListController = $controller("BooksTableListController", {
			ConfirmDialog: ConfirmDialog,
			PromotionDialog: PromotionDialog,
			ToastrNotify: ToastrNotify,
			Promotion: Promotion,
			Principal: Principal,
			Book: Book
		});
		BooksTableListController.books = books;
	}));

	describe("has promotion", function() {
		it("shoud return if the book has promotion", function() {
			expect(BooksTableListController.hasPromotion(undefined)).toBeFalsy();
			expect(BooksTableListController.hasPromotion(null)).toBeFalsy();
			var book = {};
			expect(BooksTableListController.hasPromotion(book)).toBeFalsy();
			expect(BooksTableListController.hasPromotion(book)).toBeFalsy();
			book.promotion = { newPrice: 111, start: new Date(), end: new Date() };
			expect(BooksTableListController.hasPromotion(book)).toBeTruthy();
		});
	});

	describe("is promotion started", function() {
		it("should return false if the book don't have a promotion", function() {
			var book = {};
			expect(BooksTableListController.isPromotionStarted(book)).toBeFalsy();
		});

		it("should return true when the promotion is started", function() {
			var now = new Date().getTime();
			var book = { promotion: { newPrice: 123, start: new Date(now - 60 * 1000), end: new Date(now + 1000) } };
			expect(BooksTableListController.isPromotionStarted(book)).toBeTruthy();
		});

        it("should return true when the promotion is not started", function() {
            var now = new Date().getTime();
            var book = { promotion: { newPrice: 123, start: new Date(now + 60 * 1000), end: new Date(now + 120 * 1000) } };
            expect(BooksTableListController.isPromotionStarted(book)).toBeFalsy();
        });
	});

	describe("get promotion price", function() {
		it("should return null when the book dont have a promotion", function() {
			expect(BooksTableListController.getPromotionPrice({})).toBeNull();
		});

		it("shoud return the promotion price when the book has one", function() {
            var now = new Date().getTime();
            var book = { promotion: { newPrice: 123, start: new Date(), end: new Date() } };
			expect(BooksTableListController.getPromotionPrice(book)).toEqual(book.promotion.newPrice);
		});
	});

	describe("delete book", function() {
		it("should not send a request if one is already sending", function() {
			var book = { isRemoving: true };
            spyOn(ConfirmDialog, "open");
            spyOn(Book, "remove");

            BooksTableListController.deleteBook(book);
            $rootScope.$apply();

			expect(ConfirmDialog.open).not.toHaveBeenCalled();
			expect(Book.remove).not.toHaveBeenCalled();
		});

		it("should delete the book when the dialog is confirmed", function() {
            var index = 2;
            var book = books[index];
			spyOn(ConfirmDialog, "open").and.returnValue($q.when());
			spyOn(Book, "remove").and.callFake(function() {
				expect(book.isRemoving).toBeTruthy();
                return { $promise: $q.when({}) }
			});

			BooksTableListController.deleteBook(book);
			$rootScope.$apply();

			expect(ConfirmDialog.open).toHaveBeenCalled();
			expect(Book.remove).toHaveBeenCalledWith({ slug: book.slug });
			expect(BooksTableListController.books.length).toEqual(books.length);
			expect(BooksTableListController.books.length).toEqual(size - 1);
			size--;
		});

		it("should display a error when removing the book failed", function() {
            var index = 2;
            var book = books[index];
            spyOn(ConfirmDialog, "open").and.returnValue($q.when());
            spyOn(Book, "remove").and.callFake(function() {
				expect(book.isRemoving).toBeTruthy();
                return { $promise: $q.reject({}) }
			});
			spyOn(ToastrNotify, 'error');

            BooksTableListController.deleteBook(book);
            $rootScope.$apply();

			expect(ConfirmDialog.open).toHaveBeenCalled();
            expect(Book.remove).toHaveBeenCalledWith({ slug: book.slug });
			expect(BooksTableListController.books).toEqual(books);
			expect(ToastrNotify.error).toHaveBeenCalled();
			expect(books.isRemoving).toBeFalsy();
		});

		it("should not send a request if the dialog is not confirmed", function() {
            var index = 3;
            var book = books[index];
            spyOn(ConfirmDialog, "open").and.returnValue($q.reject());
            spyOn(Book, "remove");

			BooksTableListController.deleteBook(book);
			$rootScope.$apply();

			expect(ConfirmDialog.open).toHaveBeenCalled();
			expect(Book.remove).not.toHaveBeenCalled();
			expect(books.isRemoving).toBeFalsy();
		});
	});

	describe("remove promotion", function() {
		it("should not send a request if the book dont have a promotion", function() {
            spyOn(Promotion, 'remove');

            BooksTableListController.removePromotion({});
            $rootScope.$apply();

            expect(Promotion.remove).not.toHaveBeenCalled();
		});

		it("should not send a request if one is already sending", function() {
            var book = { isPromotionRemoving: true };
			spyOn(Promotion, 'remove');

			BooksTableListController.removePromotion(book);
			$rootScope.$apply();

			expect(Promotion.remove).not.toHaveBeenCalled();
		});

		it("should set the book promotion to null when deleting successed", function() {
            var book = books[0];
            book.promotion = { newPrice: 123, start: new Date(), end: new Date() };
			spyOn(Promotion, 'remove').and.callFake(function() {
				expect(book.isPromotionRemoving).toBeTruthy();
				return { $promise: $q.when({})};
			});

			BooksTableListController.removePromotion(book);
			$rootScope.$apply();

			expect(Promotion.remove).toHaveBeenCalledWith({ slug: book.slug });
			expect(book.isPromotionRemoving).toBeFalsy();
			expect(book.promotion).toBeNull();
		});

        it("should not set the book promotion to null when deleting failed", function() {
            var book = books[2];
            book.promotion = { newPrice: 123, start: new Date(), end: new Date() };
            spyOn(Promotion, 'remove').and.callFake(function() {
                expect(book.isPromotionRemoving).toBeTruthy();
                return { $promise: $q.reject({}) };
            });

            BooksTableListController.removePromotion(book);
            $rootScope.$apply();

            expect(Promotion.remove).toHaveBeenCalledWith({ slug: book.slug });
            expect(book.isPromotionRemoving).toBeFalsy();
            expect(book.promotion).not.toBeNull();
        });
	});
});