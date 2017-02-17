(function() {
	'use strict';

	angular
		.module('app.book.directive')
		.directive('imageSelected', imageSelected);

	imageSelected.$inject = ['EVENTS'];

	function imageSelected(EVENTS) {
		var options = {
			restrict: 'A',
			require: 'ngModel',
			link: linkFunc
		};

		return options;

		function linkFunc(scope, element, attrs, ctrl) {
			element.bind('change', function(event) {
				var image = event.target.files[0];
				scope.$emit(EVENTS.bookImageSelected, { image: image });
				scope.$apply(function() {
					ctrl.$setViewValue(element.val());
					ctrl.$render();
				});
			});
		}
	}
})();
