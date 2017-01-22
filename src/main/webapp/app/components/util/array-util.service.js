(function() {
	'use strict';

	angular
		.module('app')
		.factory('ArrayUtils', ArrayUtils);

	ArrayUtils.$inject = [];

	function ArrayUtils() {
		var service = {
			flatten: flatten,
			indexOf: indexOf
		};

		return service;

		function flatten(array) {
			var result = [];
			if (isEmpty(array)) {
				return result;
			}
			angular.forEach(array, function(value, key) {
				if (angular.isArray(value)) {
					result = result.concat(value);
				} else if (angular.isObject(value)) {
					for (var prop in value) {
						var flattened = flatten(prop);
						result.concat(flattened);
					}
				} else {
					result.push(value);
				}
			});

			return result;
		}

		function indexOf(array, element, param) {
			if (isEmpty(array) || isEmpty(element) || isEmpty(param)) {
				return -1;
			}
			for (var i = 0; i < array.length; i++) {
				if (array[i][param] === element[param]) {
					return i;
				}
			}
			return -1;
		}

		function isEmpty(value) {
			return angular.isUndefined(value) || value === null;
		}
	}
})();
