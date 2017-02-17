'use strict';
describe("UserFavorites Controller Test", function() {
	var UserFavoritesController, User, ToastrNotify, $rootRouter, EVENTS, $rootScope, $q;
	var route = { params: { slug: 'test' } };

	beforeEach(angular.mock.module('app.user'));

	beforeEach(inject(function($controller, _$rootScope_, _$q_, _User_, _ToastrNotify_, _EVENTS_) {
		$rootScope = _$rootScope_;
		$q = _$q_;
		User = _User_;
		ToastrNotify = _ToastrNotify_;
		EVENTS = _EVENTS_;
		$rootRouter = {};
		$rootRouter.navigate = jasmine.createSpy('navigate');

		var scope = $rootScope.$new();
		UserFavoritesController = $controller('UserFavoritesController', {
			User: User,
			ToastrNotify: ToastrNotify,
			$rootRouter: $rootRouter,
			EVENTS: EVENTS,
			$scope: scope
		});
	}));

	describe("on router activated", function() {
		it("should set the books when loading them is successful", function() {
			var books = [{ name: "Book 1", slug: "book-1" }, { name: "Book 2", slug: "book-2" }];
			spyOn(User, 'favorites').and.callFake(function() {
				expect(UserFavoritesController.loading).toBeTruthy();
				return { $promise: $q.when(books) };
			});
			spyOn(ToastrNotify, 'error');

			UserFavoritesController.$routerOnActivate(route);
			$rootScope.$apply();

			expect(User.favorites).toHaveBeenCalledWith(route.params);
			expect(ToastrNotify.error).not.toHaveBeenCalled();
			expect(UserFavoritesController.loading).toBeFalsy();
			expect(UserFavoritesController.favorites).toEqual(books);
		});

		it("should redirect the user to the home page when the response is 404", function() {
			var response = { status: 404, data: {} };
			spyOn(User, 'favorites').and.returnValue({ $promise: $q.reject(response) });
			spyOn(ToastrNotify, 'error');

			UserFavoritesController.$routerOnActivate(route);
			$rootScope.$apply();

			expect(User.favorites).toHaveBeenCalled();
			expect(ToastrNotify.error).not.toHaveBeenCalled();
			expect($rootRouter.navigate).toHaveBeenCalledWith(['Home']);
			expect(UserFavoritesController.loading).toBeFalsy();
			expect(UserFavoritesController.favorites).toEqual([]);
		});

		it("should display a error message when the request failed and the response is not 404", function() {
			var response = { status: 500, data: {} };
			spyOn(User, 'favorites').and.returnValue({ $promise: $q.reject(response) });
			spyOn(ToastrNotify, 'error');

			UserFavoritesController.$routerOnActivate(route);
			$rootScope.$apply();

			expect(User.favorites).toHaveBeenCalled();
			expect(ToastrNotify.error).toHaveBeenCalled();
			expect($rootRouter.navigate).not.toHaveBeenCalledWith(['Home']);
			expect(UserFavoritesController.loading).toBeFalsy();
			expect(UserFavoritesController.favorites).toEqual([]);
		});
	});

	describe("logout", function() {
		it("should redirect the user to the home page on logout", function() {
			$rootScope.$broadcast(EVENTS.logoutSuccess);

			expect($rootRouter.navigate).toHaveBeenCalledWith(['Home']);
		});
	});
});