(function() {
	'use strict';

	angular
		.module('app')
		.config(toastrCustomConfig);

	toastrCustomConfig.$inject = ['toastrConfig'];

	function toastrCustomConfig(toastrConfig) {
		angular.extend(toastrConfig, {
			closeButton: true,
			tapToDismiss: false,
			positionClass: 'toast-top-center'
		});
	}
})();
