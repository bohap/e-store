'use strict';
describe("EnterKeyEvent Directive Test", function() {
	var $scope, element;

	beforeEach(angular.mock.module('app.key-event'));

	beforeEach(inject(function($compile, $rootScope) {
		$scope = $rootScope;
		var html = angular.element(
			'<input type="test"enter-key-event="test()" />'
		);
		$scope.test = jasmine.createSpy('test');
		element = $compile(html)($scope);
	}));

	it("should call the scope function when the enter key is pressed", function() {
		element.trigger({
			type: 'keypress',
			which: 13
		});

		expect($scope.test).toHaveBeenCalled();
	});

	it("should call the scope function on enter keydown", function() {
		element.trigger({
			type: 'keydown',
			which: 13
		});

		expect($scope.test).toHaveBeenCalled();
	});

	it("should not call the scope function when the enter key is not pressed", function() {
		element.trigger({
			type: 'keydown',
			which: 12
		});

		expect($scope.test).not.toHaveBeenCalled();
	});
});