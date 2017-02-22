'use strict';
describe("Number Directive Test", function() {
	var $scope, form;

	beforeEach(angular.mock.module('app.validate'));

	beforeEach(inject(function($compile, $rootScope) {
		$scope = $rootScope;
		var element = angular.element(
			'<form name="form">' +
				'<input type="text" ng-model="model.number" name="number" number />' +
			'</form>'
		);
		$scope.model = { number: undefined };
		$compile(element)($scope);
		form = $scope.form;
	}));

	it("should pass when the value is number", function() {
		var value = "1234";
		form.number.$setViewValue(value);
		$scope.$digest();

		expect($scope.model.number).toEqual(value);
		expect(form.number.$valid).toBeTruthy();
	});

	it("should not pass when the value is not a number", function() {
		var value = "abc123";
		form.number.$setViewValue(value);
		$scope.$digest();

		expect($scope.model.number).toBeUndefined();
		expect(form.number.$valid).toBeFalsy();
	});
});
