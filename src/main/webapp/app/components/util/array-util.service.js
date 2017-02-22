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

		function flatten(param) {
			var result = [];
			if (isEmpty(param)) {
				return result;
			}

			// Check if the param is only a string
			if (!angular.isArray(param) && !angular.isObject(param)) {
				result.push(param);
				return result;
			}

			if (!angular.isObject(param) && angular.isDefined(param.message)) {
				result.push(param.message);
				return result;
			}

			angular.forEach(param, function(value, key) {
				if (angular.isArray(value)) {
					result = result.concat(flatten(value));
				} else if (angular.isObject(value)) {
					for (var prop in value) {
						var flattened = flatten(value[prop]);
						flattened.forEach(function(f) {
							if (angular.isObject(f)) {
								if (angular.isUndefined(f.message) && f.message !== null) {
									result.push(prop + " " + f.message);
								}
							} else {
								result.push(f);
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
			if (!angular.isArray(array) || array.length === 0) {
				return -1;
			}
			if (!angular.isObject(element)) {
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
