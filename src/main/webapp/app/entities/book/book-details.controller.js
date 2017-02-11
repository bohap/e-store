(function () {
	'use strict';

	angular
		.module('app.book')
		.controller('BookDetailsController', BookDetailsController);

	BookDetailsController.$inject = ['Book', 'FavoriteBook', 'BasketBook', 'Principal', 'BookUtil', 'ToastrNotify',
		'EVENTS', '$scope'];

	function BookDetailsController(Book, FavoriteBook, BasketBook, Principal, BookUtil, ToastrNotify,
		EVENTS, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.isRegularUser = Principal.isRegularUser;
		vm.isBookInTheUserFavorites = isBookInTheUserFavorites;
		vm.addToFavorites = addToFavorites;
		vm.removeFromFavorites = removeFromFavorites;
		vm.isBookInTheUserBasket = isBookInTheUserBasket;
		vm.addToBasket = addToBasket;
		vm.removeFromBasket = removeFromBasket;
		vm.isBookOnPromotion = false;
		vm.bookPrice = 0;
		vm.bookDiscount = 0;
		vm.book = {};
		vm.favorite = null;
		vm.favoriteRequestSending = false;
		vm.basket = null;
		vm.basketRequestSending = false;

		function onRouterActivated(next, prev) {
			var slug = next.params.slug;
			Book.get({ slug: slug }).$promise
				.then(onBookLoadingSuccess, onBookLoadingFailed);

			function onBookLoadingSuccess(data) {
				vm.book = data;
				vm.isBookOnPromotion = BookUtil.isBookOnPromotion(vm.book);
				vm.bookPrice = BookUtil.getBookPrice(vm.book);
				vm.bookDiscount = BookUtil.getBookDiscount(vm.book);

				resolveBookFavorite();
				resolveBasketBook();
			}

			function onBookLoadingFailed(response) {
				ToastrNotify.error("Error occurred while loading the page. Please try again", "Failed");
			}
		}

		function resolveBookFavorite() {
			Principal.resolveIdentity()
				.then(onIdentityResolved);

			function onIdentityResolved(identity) {
				if (vm.isRegularUser()) {
					FavoriteBook.exists({ slug: vm.book.slug }).$promise
						.then(onFavoriteBookCheckingSuccess, onFavoriteBookCheckingFailed);
				}
			}

			function onFavoriteBookCheckingSuccess(data) {
				vm.favorite = data;
			}

			function onFavoriteBookCheckingFailed() {
				ToastrNotify.error("Some error occurred while loading the page! Please try to reload.",
					"Loading Failed");
				vm.favorite = null;
			}
		}

		function resolveBasketBook() {
			Principal.resolveIdentity()
				.then(onIdentityResolved);

			function onIdentityResolved(identity) {
				if (vm.isRegularUser()) {
					BasketBook.exists({ slug: vm.book.slug }).$promise
						.then(onBasketBookCheckingSuccess, onBasketBookCheckingFailed);
				}
			}

			function onBasketBookCheckingSuccess(data) {
				vm.basket = data;
			}

			function onBasketBookCheckingFailed() {
				ToastrNotify.error("Some error occurred while loading the page! Please try to reload.",
					"Loading Failed");
				vm.basket = null;
			}
		}

		function isBookInTheUserFavorites() {
			if (!vm.isRegularUser()) {
				return false;
			}
			if (vm.favorite === null) {
				return false;
			}
			return vm.favorite.exists;
		}

		function addToFavorites() {
			if (vm.favoriteRequestSending) {
				return;
			}
			vm.favoriteRequestSending = true;
			FavoriteBook.save({ slug: vm.book.slug }).$promise
				.then(onFavoriteAddingSuccess, onFavoriteAddingFailed);

			function onFavoriteAddingSuccess(data) {
				vm.favoriteRequestSending = false;
				vm.favorite.exists = true;
			}

			function onFavoriteAddingFailed(response) {
				vm.favoriteRequestSending = false;
				ToastrNotify.error("Error occurred while adding the book in the favorites! Plase try again",
					"Adding Faile");
			}
		}

		function removeFromFavorites() {
			if (vm.favoriteRequestSending) {
				return;
			}
			vm.favoriteRequestSending = true;
			FavoriteBook.remove({ slug: vm.book.slug }).$promise
				.then(onFavoriteRemovingSuccess, onFavoriteRemovingFailed);

			function onFavoriteRemovingSuccess(data) {
				vm.favoriteRequestSending = false;
				vm.favorite.exists = false;
			}

			function onFavoriteRemovingFailed(response) {
				vm.favoriteRequestSending = false;
				ToastrNotify.error("Error occurred while removing the book from the favorites! Plase try again",
					"Removing Faile");
			}
		}

		function isBookInTheUserBasket() {
			if (!vm.isRegularUser()) {
				return false;
			}
			if (vm.basket === null) {
				return false;
			}
			return vm.basket.exists;
		}

		function addToBasket() {
			if (vm.basketRequestSending) {
				return;
			}
			vm.basketRequestSending = true;
			BasketBook.save({ slug: vm.book.slug }).$promise
				.then(onBasketAddingSuccess, onBasketAddingFailed);

			function onBasketAddingSuccess(data) {
				vm.basketRequestSending = false;
				vm.basket.exists = true;
			}

			function onBasketAddingFailed(response) {
				vm.favoriteRequestSending = false;
				ToastrNotify.error("Error occurred while adding the book in the basket! Please try again.",
					"Adding Failed");
			}
		}

		function removeFromBasket() {
			if (vm.basketRequestSending) {
				return;
			}
			vm.basketRequestSending = true;
			BasketBook.remove({ slug: vm.book.slug }).$promise
				.then(onBasketRemovingSuccess, onBasketRemovingFailed);

			function onBasketRemovingSuccess(data) {
				vm.basketRequestSending = false;
				vm.basket.exists = false;
			}

			function onBasketRemovingFailed(response) {
				vm.basketRequestSending = false;
				ToastrNotify.error("Error occurred while removing the book from the baske! Please try again.",
					"Removing failed");
			}
		}

		var authenticationSuccess = $scope.$on(EVENTS.authenticationSuccess, function (event, data) {
			resolveBookFavorite();
			resolveBasketBook();
		});

		$scope.$on('$destroy', function () {
			authenticationSuccess();
		});
	}
})();
