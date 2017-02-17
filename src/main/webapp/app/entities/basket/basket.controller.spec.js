'use strict';
describe("Basket Controller test", function() {
	var BasketController, BasketBook, BookUtil, ToastrNotify, OrderCreateDialog, EVENTS, $rootScope, $scope, $q, $rootRouter;
	var books = [
		{ name: "Book 1", slug: "book-1", price: 123 },
		{ name: "Book 2", slug: "book-2", price: 423 },
		{ name: "Book 3", slug: "book-3", price: 7846 },
		{ name: "Book 4", slug: "book-4", price: 75893 },
		{ name: "Book 5", slug: "book-5", price: 4823 }
	];

	beforeEach(angular.mock.module('app.basket'));

	beforeEach(inject(function($controller, _$rootScope_, _$q_, _BasketBook_, _BookUtil_, _ToastrNotify_,
								_OrderCreateDialog_, _EVENTS_) {
		$rootScope = _$rootScope_;
		$scope = $rootScope.$new();
		$q = _$q_;
		BasketBook = _BasketBook_;
		BookUtil = _BookUtil_;
		ToastrNotify = _ToastrNotify_;
		OrderCreateDialog = _OrderCreateDialog_;
		EVENTS = _EVENTS_;
		$rootRouter = {};
		$rootRouter.navigate = jasmine.createSpy('navigate');

		BasketController = $controller('BasketController', {
			BasketBook: BasketBook,
			BookUtil: BookUtil,
			ToastrNotify: ToastrNotify,
			OrderCreateDialog: OrderCreateDialog,
			EVENTS: EVENTS,
			$rootRouter: $rootRouter,
			$scope: $scope
		});
	}));

	function getWithQuantity() {
		var result = [];
		books.forEach(function(book) {
			var item = {
				book: book,
				quantity: 1
			};
			result.push(item);
		});
		return result;
	}

	describe("on router activated", function() {
		it("should set the books quantity when loading them is successful", function() {
			spyOn(BasketBook, 'query').and.callFake(function() {
				expect(BasketController.loading).toBeTruthy();
				return { $promise: $q.when(books) };
			});

			BasketController.$routerOnActivate();
			$rootScope.$apply();

			expect(BasketController.items).toEqual(getWithQuantity());
			expect(BasketController.loading).toBeFalsy();
		});

		it("shoud display a error when oading failed", function() {
			spyOn(BasketBook, 'query').and.callFake(function() {
				expect(BasketController.loading).toBeTruthy();
				return { $promise: $q.reject(books) };
			});
			spyOn(ToastrNotify, 'error');

			BasketController.$routerOnActivate();
			$rootScope.$apply();

			expect(BasketBook.query).toHaveBeenCalled();
			expect(ToastrNotify.error).toHaveBeenCalled();
			expect(BasketController.loading).toBeFalsy();
		});
	});

	describe("total price", function() {
		it("should return the totel price from all books in the basket", function() {
			spyOn(BasketBook, 'query').and.returnValue({ $promise: $q.when(books) });
			spyOn(BookUtil, 'getBookPrice').and.callFake(function(book) {
				return book.price;
			});

			BasketController.$routerOnActivate();
			$rootScope.$apply();

			var items = getWithQuantity();
			var index = 3;
			BasketController.items[index].quantity = 5;
			items[index].quantity = 5;

			var total = 0;
			items.forEach(function(value) {
				total += value.book.price * value.quantity;
			});

			expect(BasketController.totalPrice()).toEqual(total);
			expect(BookUtil.getBookPrice).toHaveBeenCalledTimes(books.length);
		});
	});

	describe("remove book", function() {
		it("should remove the book from the array when the http request successed", function() {
            var response = { msg: "Book Removed" };
            spyOn(BasketBook, 'query').and.returnValue({ $promise: $q.when(books) });
            spyOn(BasketBook, 'remove').and.returnValue({ $promise: $q.when(response) });
            spyOn(ToastrNotify, 'success');

            BasketController.$routerOnActivate();

            var index = 1;
            var items = getWithQuantity();
            BasketController.removeBook(items[index]);
            $rootScope.$apply();

			expect(BasketBook.remove).toHaveBeenCalledWith({ slug: items[index].book.slug });
            expect(ToastrNotify.success).toHaveBeenCalled();
            expect(BasketController.items.length).toEqual(items.length - 1);
            items.splice(index, 1);
            expect(BasketController.items).toEqual(items);
		});
	});

	describe("open create order dialog", function() {
		it("should not open when some book quentity is invalid", function() {
            spyOn(BasketBook, 'query').and.returnValue({ $promise: $q.when(books) });
            spyOn(OrderCreateDialog, 'open');

            BasketController.$routerOnActivate();
            $rootScope.$apply();

			BasketController.items[2].quantity = -1;
			BasketController.openCreateOrderDialog();
            $rootScope.$apply();

			expect(OrderCreateDialog.open).not.toHaveBeenCalled();
		});

		it("should empty the items when the dialog closes successfully", function() {
            spyOn(BasketBook, 'query').and.returnValue({ $promise: $q.when(books) });
            spyOn(OrderCreateDialog, 'open').and.returnValue($q.when({}));

			BasketController.$routerOnActivate();
			BasketController.openCreateOrderDialog();
			$rootScope.$apply();

			expect(OrderCreateDialog.open).toHaveBeenCalled();
			expect(BasketController.items).toEqual([]);
		});

        it("should not empty the items when the dialog is dissmissed", function() {
            spyOn(BasketBook, 'query').and.returnValue({ $promise: $q.when(books) });
            spyOn(OrderCreateDialog, 'open').and.returnValue($q.reject({}));

            BasketController.$routerOnActivate();
            BasketController.openCreateOrderDialog();
            $rootScope.$apply();

            expect(OrderCreateDialog.open).toHaveBeenCalled();
            expect(BasketController.items).toEqual(getWithQuantity());
        });
	});

	describe("logout", function() {
		it("should navigate to the home route when the user is logged out", function() {
            spyOn($scope, "$on").and.callThrough();
			$rootScope.$broadcast(EVENTS.logoutSuccess);

			expect($rootRouter.navigate).toHaveBeenCalledWith(['Home']);
		});
	});
});