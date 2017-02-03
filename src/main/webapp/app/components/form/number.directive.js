(function() {
	'use strict';

	angular
		.module('app')
		.directive('number', number);

	function number() {
		var directive = {
			restrict: 'A',
			require: 'ngModel',
			link: linkFunc
		};

		return directive;

		function linkFunc(scope, element, attrs, ctrl) {
			ctrl.$validators.number = function(modelValue) {
				return angular.isDefined(modelValue) && modelValue !== null &&
					/^\d+$/.test(modelValue);
			};
		}
	}
})();
