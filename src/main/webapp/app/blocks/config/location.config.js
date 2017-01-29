(function() {
	'use strict';

	angular
		.module('app')
		.config(locationConfig);

	locationConfig.$inject = ['$locationProvider'];

	function locationConfig($locationProvider) {
		$locationProvider.html5Mode(true);
	}
})();
