(function() {
	'use strict';

	angular
		.module('app')
		.directive('formFieldError', formFieldError);

	function formFieldError() {
		return {
			restrict: 'A',
			require: 'ngModel',
			link: linkFunc
		};
	}

	function linkFunc(scope, element, attrs, ngModelCtrl) {
		var unwatch = scope.$watchGroup([
			function() {
				return ngModelCtrl.$touched;
			}, function() {
				return ngModelCtrl.$invalid;
			}, function() {
				return ngModelCtrl.$$parentForm.$submitted;
			}
		], render);
		scope.$on("$destroy", function() {
			unwatch();
		});

		function render() {
			var formSubmitted = ngModelCtrl.$$parentForm.$submitted;
			var touched = ngModelCtrl.$touched;
			var invalid = ngModelCtrl.$invalid;
			var hasError = (formSubmitted || touched) && invalid;
			var viewValue = ngModelCtrl.$viewValue;

			var fieldContainer = element.closest('.form-group');
			fieldContainer.removeClass('has-error');
			fieldContainer.removeClass('has-success');

			var errorMessages = fieldContainer.find('.form-error-messages');
			var icon = angular.element('<i></i>');
			icon.addClass('fa form-control-feedback');
			if (hasError) {
				errorMessages.show();
				icon.addClass('fa-remove');
				fieldContainer.addClass('has-error');
			} else {
				errorMessages.hide();
				if (angular.isDefined(viewValue) && viewValue !== null) {
					var empty = angular.isArray(viewValue) && viewValue.length === 0 ? true : false;
					if (!empty) {
						icon.addClass('fa-check');
						fieldContainer.addClass('has-success');
					}
				}
			}

			element.nextAll('i').remove();
			element.after(icon);
		}
	}
})();
