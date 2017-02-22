'use strict';
describe("ArrayUtils Service test", function() {
	var ArrayUtils;

	beforeEach(angular.mock.module('app.util'));

	beforeEach(inject(function(_ArrayUtils_) {
		ArrayUtils = _ArrayUtils_;
	}));

	describe("flatten", function() {
		it("should return empty array on null or undefined param", function() {
			expect(ArrayUtils.flatten()).toEqual([]);
			expect(ArrayUtils.flatten(null)).toEqual([]);
		});

		it("should return singleton when the param is not arrat or object", function() {
			expect(ArrayUtils.flatten("test")).toEqual(["test"]);
			expect(ArrayUtils.flatten(123)).toEqual([123]);
		});

		it("should return the correct array when the param is array", function() {
			var param = [1, 2, 3, [4, 5, [6, 7]]];
			var result = ArrayUtils.flatten(param);
			expect(result).toEqual([1, 2, 3, 4, 5, 6, 7]);
		});

		it("should return the correct array when the param is object of arrays", function() {
			var param = {
				param1: ["Param.1.1", "Param.1.2"],
				param2: ["Param.2.1", "Param.2.2"],
				param3: [{ message: "Param.3.3" }, {message: "Param.3.4"}]
			};
			var result = ArrayUtils.flatten(param);
			expect(result).toEqual(["Param.1.1", "Param.1.2", "Param.2.1", "Param.2.2", "Param.3.3", "Param.3.4"]);
		});
	});

	describe("index of", function() {
		it("should return -1 when the array, or the element or the param is null or undefined", function() {
			expect(ArrayUtils.indexOf(undefined, "", "")).toEqual(-1);
			expect(ArrayUtils.indexOf(null, "", "")).toEqual(-1);
			expect(ArrayUtils.indexOf([""], undefined, "")).toEqual(-1);
			expect(ArrayUtils.indexOf([""], null, "")).toEqual(-1);
			expect(ArrayUtils.indexOf([""], "", undefined)).toEqual(-1);
			expect(ArrayUtils.indexOf([""], "", null)).toEqual(-1);
		});

		it("should return -1 when the passed array is not array", function() {
			expect(ArrayUtils.indexOf("", "", "")).toEqual(-1);
		});

		it("should return -1 when the array length is 0", function() {
			expect(ArrayUtils.indexOf([], "", "")).toEqual(-1);
		});

		it("should return -1 when the element is not a object", function() {
			expect(ArrayUtils.indexOf([{ test: "" }], "", "")).toEqual(-1);
		});

		it("should return the index when the array contains the element", function() {
			var array = [{ test: "Test 1" }, { test: "Test 2" }, { test: "Test 3" }];
			var element = { test: "Test 2" };
			var param = "test";
			expect(ArrayUtils.indexOf(array, element, param)).toEqual(1);
		});

		it("should return -1 when the array dont contains the element", function() {
			var array = [{ test: "Test 1" }, { test: "Test 2" }, { test: "Test 3" }];
			var element = "Test 4";
			var param = "test";
			expect(ArrayUtils.indexOf(array, element, param)).toEqual(-1);
		});

		it("should return -1 when the array elements dont have the given property", function() {
			var array = [{ test: "Test 1" }, { test: "Test 2" }, { test: "Test 3" }];
			var element = "Test 1";
			var param = "msg";
			expect(ArrayUtils.indexOf(array, element, param)).toEqual(-1);
		});
	});
});
