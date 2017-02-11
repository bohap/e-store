(function() {
	'use strict';

	var home = {
		templateUrl: 'app/home/home.html',
		controller: 'HomeController',
		controllerAs: 'vm'
	};

	angular
		.module('app.home')
		.component('home', home);
})();
