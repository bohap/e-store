(function() {
	'use strict';

	angular
		.module('app')
		.factory('DateUtil', DateUtil);

	function DateUtil() {
		var service = {
			addDays: addDays,
			equals: equals
		};

		return service;

		function addDays(date, days) {
			if (angular.isUndefined(date) || date === null) {
				return new Date();
			}
			var daysMills = days * 24 * 60 * 60 * 1000;
			return new Date(date.getTime() + daysMills);
		}

		function equals(date1, date2) {
			if (angular.isUndefined(date1) || date1 === null ||
				angular.isUndefined(date2) || date2 === null) {
				return false;
			}

			return date1.getTime() === date2.getTime();
		}
	}
})();
