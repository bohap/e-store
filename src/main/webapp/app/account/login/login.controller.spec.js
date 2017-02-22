'use strict';
describe("Login Controller Test", function() {
	var LoginController, $q, Auth, $uibModalInstance, ArrayUtils, $rootRouter, $rootScope;
	var route = "/test";
	var credentials = {
		username: 'test@user.com',
		password: 'pass'
	};

	beforeEach(angular.mock.module('app.account'));

	beforeEach(inject(function(_$controller_, _$q_, _$rootScope_, _Auth_, _ArrayUtils_) {
		Auth = _Auth_;
		ArrayUtils = _ArrayUtils_;
		$q = _$q_;
		$rootScope = _$rootScope_;

		// mainly mock the $uibModalInstance
		$uibModalInstance = {};
		$uibModalInstance.close = jasmine.createSpy('close');
		$uibModalInstance.dismiss = jasmine.createSpy('dismiss');

		// mainly mock the $rootRouter
		$rootRouter = {};
		$rootRouter.lastNavigationAttempt = route;
		$rootRouter.navigateByUrl = jasmine.createSpy('navigateByUrl');

		LoginController = _$controller_('LoginController', {
			Auth: Auth,
			$uibModalInstance: $uibModalInstance,
			ArrayUtils: ArrayUtils,
			$rootRouter: $rootRouter
		});
	}));

	describe("login", function() {
		it("should not send a request if one is already sending", function() {
			spyOn(Auth, 'login');
			LoginController.isSending = true;
			LoginController.login();

			expect(Auth.login).not.toHaveBeenCalled();
		});

		it("should navigate to the last route on successful login", function() {
			LoginController.username = credentials.username;
			LoginController.password = credentials.password;
			LoginController.errors = ["Err1", "Err2", "Err3", "Err4"];
			spyOn(Auth, 'login').and.callFake(function() {
				expect(LoginController.isSending).toBeTruthy();
				expect(LoginController.errors).toEqual([]);
				return $q.when({ msg: "Login Success" });
			});

			LoginController.login();
			$rootScope.$apply();

			expect(Auth.login).toHaveBeenCalledWith(credentials);
			expect($uibModalInstance.close).toHaveBeenCalled();
			expect($rootRouter.navigateByUrl).toHaveBeenCalledWith(route);
			expect(LoginController.isSending).toBeFalsy();
		});

		it("should show the errors on failed login", function() {
			var response = {
				errors: ["Err1", "Err2", "Err3", "Err4"]
			};
			LoginController.username = credentials.username;
			LoginController.password = credentials.password;
			LoginController.errors = response.errors;
			spyOn(Auth, 'login').and.callFake(function() {
				expect(LoginController.isSending).toBeTruthy();
				expect(LoginController.errors).toEqual([]);
				return $q.reject(response);
			});
			spyOn(ArrayUtils, 'flatten').and.returnValue(response.errors);

			LoginController.login();
			$rootScope.$apply();

			expect(Auth.login).toHaveBeenCalledWith(credentials);
			expect($uibModalInstance.close).not.toHaveBeenCalled();
			expect($rootRouter.navigateByUrl).not.toHaveBeenCalled();
			expect(ArrayUtils.flatten).toHaveBeenCalledWith(response.errors);
			expect(LoginController.isSending).toBeFalsy();
			expect(LoginController.errors).toEqual(response.errors);
		});

		it("should not flatten the errors when failed login response dont have any", function() {
			LoginController.username = credentials.username;
			LoginController.password = credentials.password;
			spyOn(Auth, 'login').and.returnValue($q.reject({ status: 401 }));
			spyOn(ArrayUtils, 'flatten');

			LoginController.login();
			$rootScope.$apply();

			expect(ArrayUtils.flatten).not.toHaveBeenCalled();
		});
	});
});
