(function() {
	'use strict';

	angular
		.module('app')
		.directive('enterPress', enterPress);

		function enterPress() {
			var directive = {
				restrict: 'A',
				link: linkFunc
			};

			return directive;

			function linkFunc(scope, element, attrs) {
				element.bind("keydown keypress", function(event) {
					if (event.which === 13) {
						scope.$apply(function() {
							scope.$eval(attrs.enterPress);
						});

						event.preventDefault();
					}
				});
			}
		}
})();
