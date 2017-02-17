'use strict';
describe("Home Controller test", function() {
	var HomeController, Book, BookUtil, ToastrNotify, $q, $rootScope;

	beforeEach(angular.mock.module('app.home'));

	beforeEach(inject(function($controller, _$q_, _$rootScope_, _Book_, _BookUtil_, _ToastrNotify_) {
		$rootScope = _$rootScope_;
		$q = _$q_;
		Book = _Book_;
		BookUtil = _BookUtil_;
		ToastrNotify = _ToastrNotify_;

		HomeController = $controller('HomeController', {
			Book: Book,
			BookUtil: BookUtil,
			ToastrNotify: ToastrNotify
		});
	}));

	describe("on router activate", function() {
		it("should set the popular book when loading them is successful", function() {
			var books = ["Book 1", "Book 2"];
			spyOn(Book, 'popular').and.callFake(function() {
				expect(HomeController.popularLoading).toBeTruthy();
				return { $promise: $q.when(books) };
			});

			HomeController.$routerOnActivate();
			$rootScope.$apply();

			expect(Book.popular).toHaveBeenCalled();
			expect(HomeController.popularLoading).toBeFalsy();
			expect(HomeController.popular).toEqual(books);
		});

		it("should display a error when loading the popular books failed", function() {
			spyOn(Book, 'popular').and.callFake(function() {
				expect(HomeController.popularLoading).toBeTruthy();
				return { $promise: $q.reject() };
			});
			spyOn(ToastrNotify, 'error');

			HomeController.$routerOnActivate();
			$rootScope.$apply();

			expect(Book.popular).toHaveBeenCalledWith({ limit: 10 });
			expect(ToastrNotify.error).toHaveBeenCalled();
			expect(HomeController.popularLoading).toBeFalsy();
			expect(HomeController.popular).toEqual([]);
		});

		it("should set the latest book when loading them is successful", function() {
			var books = ["Book 1", "Book 2"];
			spyOn(Book, 'query').and.callFake(function() {
				expect(HomeController.latestLoading).toBeTruthy();
				return { $promise: $q.when(books) };
			});

			HomeController.$routerOnActivate();
			$rootScope.$apply();

			expect(Book.query).toHaveBeenCalledWith({ limit: 10, latest: true });
			expect(HomeController.latestLoading).toBeFalsy();
			expect(HomeController.latest).toEqual(books);
		});

		it("should display a error when loading the popular books failed", function() {
			spyOn(Book, 'query').and.callFake(function() {
				expect(HomeController.latestLoading).toBeTruthy();
				return { $promise: $q.reject() };
			});
			spyOn(ToastrNotify, 'error');

			HomeController.$routerOnActivate();
			$rootScope.$apply();

			expect(Book.query).toHaveBeenCalled();
			expect(ToastrNotify.error).toHaveBeenCalled();
			expect(HomeController.latestLoading).toBeFalsy();
			expect(HomeController.latest).toEqual([]);
		});
	});
});