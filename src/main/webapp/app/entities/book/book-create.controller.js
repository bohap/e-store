(function() {
	'use strict';

	angular
		.module('app.book')
		.controller('BookCreateController', BookCreateController);

	BookCreateController.$inject = ['Book', 'ArrayUtils', 'EVENTS', '$rootRouter', '$scope'];

	function BookCreateController(Book, ArrayUtils, EVENTS, $rootRouter, $scope) {
		var vm = this;

		vm.book = {
			hasImage: false
		};
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
				if (angular.isDefined(response.data) && response.data !== null &&
					angular.isDefined(response.data.errors) && response.data.errors !== null ) {
					vm.errors = ArrayUtils.flatten(response.data.errors);
				}
			}
		}

		var logoutWatch = $scope.$on(EVENTS.logoutSuccess, function(event, date) {
			$rootRouter.navigate(['Home']);
		});

		$scope.$on('$destroy', function() {
			logoutWatch();
		});
	}
})();
