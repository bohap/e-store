(function() {
	'use strict';

	angular
		.module('app')
		.controller('OrderCreateController', OrderCreateController);

	OrderCreateController.$inject = ['items', '$uibModalInstance', 'BookUtil', 'Basket', 'toastr'];

	function OrderCreateController(items, $uibModalInstance, BookUtil, Basket, toastr) {
		var vm = this;
		var tabsSize = 3;
		var addressTabIndex = 1;

		vm.items = items;
		vm.close = close;
		vm.next = next;
		vm.prev = prev;
		vm.hasNext = hasNext;
		vm.hasPrev = hasPrev;
		vm.isTabPassed = isTabPassed;
		vm.canCreate = canCreate;
		vm.create = create;
		vm.getBookPrice = BookUtil.getBookPrice;
		vm.total = totalPrice();
		vm.sending = false;
		vm.activeTab = 0;
		vm.address = {};
		vm.creditCard = {};
		vm.nowYear = new Date().getFullYear();

		function close() {
			$uibModalInstance.dismiss('canceled');
		}

		function totalPrice() {
			var total = 0;
			vm.items.forEach(function(item) {
				var price = vm.getBookPrice(item.book);
				total += item.quantity * price;
			});
			return total;
		}

		function next() {
			vm.activeTab++;
		}

		function prev() {
			vm.activeTab--;
		}

		function isTabPassed(index) {
			return index < vm.activeTab;
		}

		function hasNext() {
			if (vm.activeTab === addressTabIndex) {
				return !isEmpty(vm.address.type) &&
						!isEmpty(vm.address.street) &&
						!isEmpty(vm.address.city) &&
						!isEmpty(vm.address.state) &&
						!isEmpty(vm.address.postalCode) &&
						!isEmpty(vm.address.countryCode);
			}
			return vm.activeTab < tabsSize - 1;
		}

		function hasPrev() {
			return vm.activeTab > 0;
		}

		function canCreate() {
			if (vm.activeTab < tabsSize - 1) {
				return false;
			}
			return !isEmpty(vm.creditCard.type) &&
					!isEmpty(vm.creditCard.number) &&
					!isEmpty(vm.creditCard.expireMonth) &&
					!isEmpty(vm.creditCard.expireYear) &&
					!isEmpty(vm.creditCard.cvv2);
		}

		function isEmpty(value) {
			return angular.isUndefined(value) || value === null;
		}

		function create() {
			if (vm.sending) {
				return;
			}

			var items = [];
			vm.items.forEach(function(i) {
				var item = {
					slug: i.book.slug,
					quantity: i.quantity
				};
				items.push(item);
			});

			var data = {
				billingAddress: vm.address,
				creditCard: vm.creditCard,
				books: items
			};

			vm.sending = true;
			Basket.checkout(data).$promise
				.then(onOrderCreatingSuccess, onOrderCreatingFailed);

			function onOrderCreatingSuccess() {
				vm.sending = false;
				toastr.success("Order is created successfully", "Order Created");
				$uibModalInstance.close('created');
			}

			function onOrderCreatingFailed() {
				vm.sending = false;
				toastr.error("Error occurred while creating the order. Plase try again", "Failed");
			}
		}
	}
})();
