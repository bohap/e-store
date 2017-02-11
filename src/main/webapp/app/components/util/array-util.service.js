(function() {
	'use strict';

	angular
		.module('app.util')
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

			// Check if the param is only a string
			if (!angular.isArray(array) && !angular.isObject(array)) {
				result.push(array);
				return result;
			}

			angular.forEach(array, function(value, key) {
				if (angular.isArray(value)) {
					result = result.concat(value);
				} else if (angular.isObject(value)) {
					for (var prop in value) {
						var flattened = flatten(prop);
						flattened.forEach(function(f) {
							if (angular.isUndefined(f.message) && f.message !== null) {
								result.push(prop + " " + f.message);
							}
						});
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
