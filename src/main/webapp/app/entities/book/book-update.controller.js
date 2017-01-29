(function() {
	'use strict';

	angular
		.module('app')
		.controller('BookUpdateController', BookUpdateController);

	BookUpdateController.$inject = ['Book', 'ArrayUtils', 'EVENTS', '$rootRouter', '$scope'];

	function BookUpdateController(Book, ArrayUtils, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.$routerOnActivate = onRouterActivated;
		vm.update = update;
		vm.book = {};
		vm.errors = [];
		vm.sending = false;

		function onRouterActivated(next, prev) {
			var slug = next.params.slug;
			Book.get({slug: slug}).$promise
				.then(onBookLoadingSuccess, onBookLoadingFailed);

			function onBookLoadingSuccess(data) {
				vm.book = data;
				vm.book.hasImage = true;
			}

			function onBookLoadingFailed(response) {
				vm.book = {};
				vm.$router.navigate(['Book']);
			}
		}

		function update(image) {
			if (vm.sending) {
				return;
			}

			vm.sending = true;
			Book.update({slug: vm.book.slug, book: vm.book, image: image}).$promise
				.then(onBookUpdatingSuccess, onBookUpdatingFailed);

			function onBookUpdatingSuccess(data) {
				vm.sending = false;
				vm.$router.navigate(['BookDetails', {slug: vm.book.slug}]);
			}

			function onBookUpdatingFailed(response) {
				vm.sending = false;
				if (angular.isDefined(response.data.errors)) {
					vm.errors = ArrayUtils.flatten(response.data.errors);
				}
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
