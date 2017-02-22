'use strict';
describe("Register Controller test", function() {
	var RegisterController, Auth, $uibModalInstance, ArrayUtils, $q, $rootScope;
	var account = {
		name: 'Test User',
		email: 'test@user.com',
		password: 'pass',
		passwordConfirmation: "pass"
	};

	beforeEach(angular.mock.module('app.account'));

	beforeEach(inject(function($controller, _$rootScope_, _$q_, _Auth_, _ArrayUtils_) {
		Auth = _Auth_;
		ArrayUtils = _ArrayUtils_;
		$rootScope = _$rootScope_;
		$q = _$q_;

		$uibModalInstance = {};
		$uibModalInstance.dismiss = jasmine.createSpy('dismiss');
		$uibModalInstance.close = jasmine.createSpy('close');

		RegisterController = $controller('RegisterController', {
			Auth: Auth,
			$uibModalInstance: $uibModalInstance,
			ArrayUtils: ArrayUtils
		});
	}));

	describe("register", function() {
		it("should not send a request is one is already sending", function() {
			spyOn(Auth, 'register');
			RegisterController.isSending = true;
			RegisterController.register();

			expect(Auth.register).not.toHaveBeenCalled();
		});

		it("should close the modal on successful registration", function() {
			RegisterController.name = account.name;
			RegisterController.username = account.email;
			RegisterController.password = account.password;
			RegisterController.repeatPassword = account.passwordConfirmation;
			RegisterController.errors = ["Err1", "Err2", "Err3"];
			spyOn(Auth, 'register').and.callFake(function() {
				expect(RegisterController.isSending).toBeTruthy();
				expect(RegisterController.errors).toEqual([]);
				return $q.when({});
			});

			RegisterController.register();
			$rootScope.$apply();

			expect(RegisterController.isSending).toBeFalsy();
			expect(Auth.register).toHaveBeenCalledWith(account);
			expect($uibModalInstance.close).toHaveBeenCalled();
		});

		it("should show the errors on failed registration", function() {
			var response = {
				errors: ["Err1", "Err2", "Err3"]
			};
			RegisterController.name = account.name;
			RegisterController.username = account.email;
			RegisterController.password = account.password;
			RegisterController.repeatPassword = account.passwordConfirmation;
			RegisterController.errors = ["Err1", "Err2", "Err3"];
			spyOn(Auth, 'register').and.callFake(function() {
				expect(RegisterController.isSending).toBeTruthy();
				expect(RegisterController.errors).toEqual([]);
				return $q.reject(response);
			});
			spyOn(ArrayUtils, 'flatten').and.returnValue(response.errors);

			RegisterController.register();
			$rootScope.$apply();

			expect(RegisterController.isSending).toBeFalsy();
			expect(Auth.register).toHaveBeenCalledWith(account);
			expect($uibModalInstance.close).not.toHaveBeenCalled();
			expect(ArrayUtils.flatten).toHaveBeenCalledWith(response.errors);
			expect(RegisterController.errors).toEqual(response.errors);
		});

		it("should not flatten the errors when failed registration response dont have any", function() {
			spyOn(Auth, 'register').and.returnValue($q.reject({}));
			spyOn(ArrayUtils, 'flatten');

			RegisterController.register();
			$rootScope.$apply();

			expect(ArrayUtils.flatten).not.toHaveBeenCalled();
			expect(RegisterController.errors).toEqual([]);
		});
	});
});
