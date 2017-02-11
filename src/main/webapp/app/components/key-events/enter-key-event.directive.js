(function() {
	'use strict';

	angular
		.module('app.key-event')
		.directive('enterKeyEvent', enterKeyEvent);

	function enterKeyEvent() {
		var directive = {
			restrict: 'A',
			link: linkFunc
		};

		return directive;

		function linkFunc(scope, element, attrs) {
			element.bind("keydown keypress", function(event) {
				if (event.which === 13) {
					scope.$apply(function() {
						scope.$eval(attrs.enterKeyEvent);
					});

					event.preventDefault();
				}
			});
		}
	}
})();
