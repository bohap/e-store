(function() {
	'use strict';

	angular.module('app')
		.directive('hoverToggleClass', faHeartDirective);

	function faHeartDirective() {
		var directive = {
			restrict: 'A',
			link: linkFunc,
			scope: {
				element: '@',
				toAdd: '@',
				toRemove: '@'
			}
		};

		return directive;

		function linkFunc(scope, element, attrs) {

			element.on('mouseover', function() {
				toggleClasses(scope.toRemove, scope.toAdd);
			});

			element.on('mouseleave', function() {
				toggleClasses(scope.toAdd, scope.toRemove);
			});

			function toggleClasses(toRemove, toAdd) {
				var el = element.find(scope.element);
				el.removeClass(toRemove);
				el.addClass(toAdd);
			}
		}
	}
})();
