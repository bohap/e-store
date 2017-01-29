(function() {
	'use strict';

	angular
		.module('app')
		.directive('uiSelectRequired', uiSelectRequired);

	function uiSelectRequired() {
		var options = {
			restrict: 'A',
			require: 'ngModel',
			link: linkFunc
		};

		return options;

		function linkFunc(scope, element, attrs, ctrl) {
			ctrl.$validators.uiSelectRequired = function(modelValue) {
				return modelValue && modelValue.length > 0;
			}
		}
	}
})();
