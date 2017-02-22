'use strict';
describe("FieldMatch Directive Test", function() {
	var $scope, form;

	beforeEach(angular.mock.module('app.validate'));

	beforeEach(inject(function($compile, $rootScope) {
		$scope = $rootScope;
		var element = angular.element(
			'<form name="form">' +
				'<input type="text" ng-model="model.original" name="original" /> ' +
				'<input type="text" ng-model="model.matching" name="matching" field-match="model.original" /> ' +
			'</form>'
		);
		$scope.model = {
			original: undefined,
			matching: undefined
		};
		$compile(element)($scope);

		form = $scope.form;
	}));

	it("should pass when the field have the same value", function() {
		var value = "Test";
		form.original.$setViewValue(value);
		form.matching.$setViewValue(value);
		$scope.$digest();

		expect($scope.model.original).toEqual(value);
		expect($scope.model.matching).toEqual(value);
		expect(form.matching.$valid).toBeTruthy();
	});

	it("should pass when the field have the same value", function() {
		var original = "Original";
		var matching = "Matching";
		form.original.$setViewValue(original);
		form.matching.$setViewValue(matching);
		$scope.$digest();

		expect($scope.model.original).toEqual(original);
		expect($scope.model.matching).toBeUndefined();
		expect(form.matching.$valid).toBeFalsy();
	});
});
