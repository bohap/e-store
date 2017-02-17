'use strict';
describe("ValidFile Directive Test", function() {
	var $scope, form, element, EVENTS;

	beforeEach(angular.mock.module('app.book.directive'));

	beforeEach(inject(function($compile, $rootScope, _EVENTS_) {
		$scope = $rootScope;
		EVENTS = _EVENTS_;
		var html = angular.element(
			'<form>' +
				'<input type="file" ng-model="model.image" name="image" image-selected />' +
			'</form>'
		);
		$scope.model = { image: undefined };
		element = $compile(html)($scope);
		$scope.$digest();

		form = $scope.form;
	}));

	it("should emit a broadcast when the image is selected", function() {
		spyOn($scope, '$emit');
		expect($scope.model.image).toBeUndefined();

		var image = {
			name: 'test.png',
			size: 500000,
			type: 'image/png'
		};
		var images = {
			0: image,
			length: 1,
			item: function(index) { return image; }
		};

		var input = element.find('input');
		spyOn($.fn, "val").and.returnValue(image.name);
		input.trigger({
			type: 'change',
			target: {
				files: images
			}
		});

		expect($scope.$emit).toHaveBeenCalledWith(EVENTS.bookImageSelected, { image: image });
		expect($scope.model.image).toEqual(image.name);
	});
});