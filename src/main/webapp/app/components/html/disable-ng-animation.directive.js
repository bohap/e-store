(function() {
	'use strict';

	angular
		.module('app.html')
		.directive('disableNgAnimation', disableNgAnimation);

	disableNgAnimation.$inject = ['$animate'];

	function disableNgAnimation($animate) {
		return {
			restrict: 'A',
			link: linkFunc
		};

		function linkFunc(scope, element) {
			$animate.enabled(element, false);
		}
	}
})();
