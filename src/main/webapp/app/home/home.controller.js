(function() {
	'use strict';

	angular
		.module('app')
		.controller('HomeController', HomeController);

	HomeController.$inject = ['Book', 'BookUtil', 'toastr'];

	function HomeController(Book, BookUtil, toastr) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivate;
		vm.isBookOnPromotion = BookUtil.isBookOnPromotion;
		vm.popular = [];
		vm.latest = [];
		vm.popularLoaded = false;
		vm.latestLoaded = false;

		function onRouterActivate() {
			Book.popular({limit: 10}).$promise
				.then(onPopularBooksLoadingSuccess, onLoadingFailed);

			function onPopularBooksLoadingSuccess(books) {
				vm.popular = books;
				vm.popularLoaded = true;
			}

			Book.query({limit: 10, latest: true}).$promise
				.then(onLatestBooksLoadingSuccess, onLoadingFailed);

			function onLatestBooksLoadingSuccess(books) {
				vm.latest = books;
				vm.latestLoaded = true;
			}

			function onLoadingFailed() {
				toastr.error("Error occurred while loading the page. Please reload it.",
					"Loading Failed");
			}
		}
	}
})();
