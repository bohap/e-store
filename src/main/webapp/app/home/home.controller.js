(function() {
	'use strict';

	angular
		.module('app.home')
		.controller('HomeController', HomeController);

	HomeController.$inject = ['Book', 'BookUtil', 'ToastrNotify'];

	function HomeController(Book, BookUtil, ToastrNotify) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivate;
		vm.isBookOnPromotion = BookUtil.isBookOnPromotion;
		vm.popular = [];
		vm.latest = [];
		vm.popularLoading = false;
		vm.latestLoading = false;

		function onRouterActivate() {
			vm.popularLoading = true;
			Book.popular({limit: 10}).$promise
				.then(onPopularBooksLoadingSuccess, onLoadingFailed);

			function onPopularBooksLoadingSuccess(books) {
				vm.popular = books;
				vm.popularLoading = false;
			}

			vm.latestLoading = true;
			Book.query({limit: 10, latest: true}).$promise
				.then(onLatestBooksLoadingSuccess, onLoadingFailed);

			function onLatestBooksLoadingSuccess(books) {
				vm.latest = books;
				vm.latestLoading = false;
			}

			function onLoadingFailed() {
				vm.popularLoading = false;
				vm.latestLoading = false;
				ToastrNotify.error("Error occurred while loading the page. Please reload it.",
					"Loading Failed");
			}
		}
	}
})();
