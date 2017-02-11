(function() {
	'use strict';

	angular
		.module('app.promotion.dialog')
		.controller('PromotionCreateController', PromotionCreateController);

	PromotionCreateController.$inject = ['book', '$uibModalInstance', 'Promotion', 'ArrayUtils', 'DateUtil'];

	function PromotionCreateController(book, $uibModalInstance, Promotion, ArrayUtils, DateUtil) {
		var vm = this;

		vm.cancel = cancel;
		vm.save = save;
		vm.openStartDatepicker = openStartDatepicker;
		vm.openEndDatepicker = openEndDatepicker;
		vm.updateDatepickerOptions = updateDatepickerOptions;
		vm.book = book;
		vm.promotion = getPromotion();
		vm.errors = [];
		vm.sending = false;
		vm.startDatepickerOpened = false;
		vm.endDatepickerOpened = false;
		vm.startDatepickerOptions = {
			minDate: new Date()
		};
		vm.endDatepickerOptions = {
			minDate: new Date()
		};

		function getPromotion() {
			if (angular.isUndefined(book.promotion) || book.promotion === null) {
				return {};
			} else {
				var result = angular.fromJson(angular.toJson(vm.book.promotion));
				result.start = new Date(result.start);
				result.end = new Date(result.end);
				return result;
			}
		}

		function openStartDatepicker() {
			vm.startDatepickerOpened = true;
		}

		function openEndDatepicker() {
			vm.endDatepickerOpened = true;
		}

		function updateDatepickerOptions() {
			vm.startDatepickerOptions.maxDate = vm.promotion.end;
			vm.endDatepickerOptions.minDate = DateUtil.addDays(vm.promotion.start, 1);

			if (DateUtil.equals(vm.promotion.start, vm.promotion.end)) {
				vm.promotion.end = DateUtil.addDays(vm.promotion.end, 1);
			}
		}

		function cancel() {
			$uibModalInstance.dismiss("canceled");
		}

		function save() {
			if (vm.sending) {
				return;
			}

			vm.sending = true;
			vm.errors = [];
			Promotion.save({slug: book.slug}, vm.promotion).$promise
				.then(onPromotionAddingSuccess, onPromotionAddingFailed);

			function onPromotionAddingSuccess(data) {
				vm.sending = false;
				vm.book.promotion = vm.promotion;
				$uibModalInstance.close('saved');
			}

			function onPromotionAddingFailed(response) {
				vm.sending = false;
				if (angular.isDefined(response.data.errors) && response.data.errors !== null) {
					vm.errors = ArrayUtils.flatten(response.data.errors);
				}
			}
		}
	}
})();
