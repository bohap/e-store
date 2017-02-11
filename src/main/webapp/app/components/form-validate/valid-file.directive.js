(function() {
	'use strict';

	angular
		.module('app.validate')
		.directive('validFile', validFile);

	validFile.$inject = ['EVENTS'];

	function validFile(EVENTS) {
		var options = {
			restrict: 'A',
			require: 'ngModel',
			link: linkFunc
		};

		return options;

		function linkFunc(scope, element, attrs, ctrl) {
			element.bind('change', function(event) {
				var file = event.target.files[0];
				scope.$emit(EVENTS.bookImageSelected, {file: file});
				scope.$apply(function(){
					ctrl.$setViewValue(element.val());
					ctrl.$render();
				});
			});
		}
	}
})();
