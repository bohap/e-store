(function() {
	'use strict';

	angular
		.module('app')
		.controller('ErrorsListController', ErrorsListController);

	ErrorsListController.$inject = [];

	function ErrorsListController() {
		var vm = this;

		vm.hasErrors = hasErrors;

		function hasErrors() {
			return angular.isDefined(vm.errors) && vm.errors.length > 0;
		}
	}
})();
