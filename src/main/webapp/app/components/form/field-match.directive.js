(function() {
	'use strict';

	angular
		.module('app')
		.directive('fieldMatch', fieldMatch);

	function fieldMatch() {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				field: '=fieldMatch'
			},
			link: linkFunc
		};

		function linkFunc(scope, element, attr, ngModelCtrl) {
			ngModelCtrl.$validators.fieldMatch = function(modelValue, viewValue) {
				return modelValue !== null && modelValue === scope.field;
			};

			var unwatch = scope.$watch('field', function() {
				ngModelCtrl.$validate();
			});

			scope.$on("$destroy", function() {
				unwatch();
			});
		}
	}
})();
