(function() {
	'use strict';

	angular
		.module('app.book')
		.controller('BookFormController', BookFormController);

	BookFormController.$inject = ['EVENTS', '$scope', 'Category', 'Book'];

	function BookFormController(EVENTS, $scope, Category, Book) {
		var vm = this;

		vm.$onInit = init;
		vm.categoryTransform = categoryTransform;
		vm.deleteImage = deleteImage;
		vm.submit = submit;
		vm.categories = [];
		var image = null;

		function init() {
			Category.query().$promise
				.then(onCategoriesLoadingSuccess, onCategoriesLoadingFailed);

			function onCategoriesLoadingSuccess(response) {
				vm.categories = response;
			}

			function onCategoriesLoadingFailed(response) {
				vm.categories = [];
			}
		}

		function categoryTransform(category) {
			var item = {
				name: category,
				id: vm.categories.length + 1
			};

			return item;
		}

		function deleteImage() {
			vm.book.hasImage = false;
		}

		function submit() {
			vm.save({image: image});
		}

		var unwatch = $scope.$on(EVENTS.bookImageSelected, function(event, data) {
			image = data.file;
		});

		$scope.$on('$destroy', function() {
			unwatch();
		});
	}
})();
