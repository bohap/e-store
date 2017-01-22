(function() {
	'use strict';

	angular
		.module('app')
		.factory('DateUtil', DateUtil);

	function DateUtil() {
		var service = {
			getYearDiff: getYearDiff
		};

		return service;

		function getYearDiff(date1, date2) {
			var yearMills = 356 * 24 * 60 * 60 * 1000;
			var diff = (date1.getTime() - date2.getTime()) / yearMills;
			return Math.floor(diff);
		}
	}
})();
