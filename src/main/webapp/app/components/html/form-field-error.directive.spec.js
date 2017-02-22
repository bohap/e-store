'use strict';
describe("FormFieldError Directive Test", function() {
	var form, element, $scope, group, messages, input;

	beforeEach(angular.mock.module('app.html'));

	beforeEach(inject(function($compile, $rootScope) {
		$scope = $rootScope;
		var html = angular.element(
			'<form name="form">' +
				'<div class="form-group">' +
					'<div>' +
						'<input type="text" id="test" name="test" ng-model="test" required form-field-error />' +
					'</div>' +
					'<div class="form-error-messages"></div>' +
				'</div>' +
			'</form>'
		);
		$scope.test = undefined;
		element = $compile(html)($scope);
		$scope.$digest();

		form = $rootScope.form;
		group = element.find('.form-group');
		messages = element.find('.form-error-messages');
		input = element.find('input');
	}));

	it("should not set the classes when the field is not touched and the form is nto submitted", function() {
		expect(messages.css('display')).toEqual('none');

		var icon = input.parent().find('i');
		expect(icon.hasClass('fa-remove')).toBeFalsy();
		expect(icon.hasClass('fa-check')).toBeFalsy();

		expect(group.hasClass('has-error')).toBeFalsy();
		expect(group.hasClass('has-success')).toBeFalsy();
	});

	it("should set the classes when the field is touched and is invalid", function() {
		form.test.$setTouched(true);
		form.test.$setViewValue(undefined);
		$scope.$digest();

		expectToHaveErrorClasses();
	});

	it("should set the classes when the field is not touched but the form is submitted", function() {
		form.$setSubmitted();
		$scope.$digest();

		expectToHaveErrorClasses();
	});

	it("should set the classes when the field is touched and its valid", function() {
		form.test.$setTouched(true);
		form.test.$setViewValue("Test");
		$scope.$digest();

		expect(messages.css('display')).toEqual('none');

		var icon = input.parent().find('i');
		expect(icon.hasClass('fa-check')).toBeTruthy();
		expect(icon.hasClass('fa-remove')).toBeFalsy();

		expect(group.hasClass('has-success')).toBeTruthy();
		expect(group.hasClass('has-error')).toBeFalsy();
	});

	function expectToHaveErrorClasses() {
		expect(messages.css('display')).toEqual('');

		var icon = input.parent().find('i');
		expect(icon.hasClass('fa-remove')).toBeTruthy();
		expect(icon.hasClass('fa-check')).toBeFalsy();

		expect(group.hasClass('has-error')).toBeTruthy();
		expect(group.hasClass('has-success')).toBeFalsy();
	}
});
