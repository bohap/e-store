(function() {
	'use strict';

	angular
		.module('app')
		.controller('BookCreateController', BookCreateController);

	BookCreateController.$inject = ['Book', 'ArrayUtils', 'EVENTS', '$rootRouter', '$scope'];

	function BookCreateController(Book, ArrayUtils, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.book = {};
		vm.errors = [];
		vm.save = save;
		vm.sending = false;

		function save(image) {
			if (vm.sending) {
				return;
			}

			vm.sending = true;
			Book.save({book: vm.book, image: image}).$promise
				.then(onBookSavingSuccess, onBookSavingFailed);

			function onBookSavingSuccess(data) {
				vm.sending = false;
				var slug = data.slug;
				vm.$router.navigate(['BookDetails', {slug: slug}]);
			}

			function onBookSavingFailed(response) {
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
