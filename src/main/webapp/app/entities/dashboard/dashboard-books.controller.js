(function() {
	'use strict';

	angular
		.module('app')
		.controller('DasboardBooksController', DasboardBooksController);

	DasboardBooksController.$inject = ['Book', 'EVENTS', '$rootRouter', '$scope', 'toastr'];

	function DasboardBooksController(Book, EVENTS, $rootRouter, $scope, toastr) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.books = [];
		vm.areBooksLoaded = false;

		function onRouterActivated() {
			Book.query({latest: true}).$promise
				.then(onBooksLoadingSuccess, onBooksLoadingFailed);

			function onBooksLoadingSuccess(data) {
				vm.books = data;
				vm.areBooksLoaded = true;
			}

			function onBooksLoadingFailed(response) {
				vm.books = [];
				vm.areBooksLoaded = false;
				toastr.error("Error occurred while loadin the page! Please reload it.", "Loading Failed");
			}
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Book']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
