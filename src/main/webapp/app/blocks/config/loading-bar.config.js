(function() {
	'use strict';

	angular
		.module('app')
		.config(loadingBarConfig);

	loadingBarConfig.$injector = ['cfpLoadingBarProvider'];

	function loadingBarConfig(cfpLoadingBarProvider) {
		cfpLoadingBarProvider.latencyThreshold = 10;
	}
})();
