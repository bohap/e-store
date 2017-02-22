'use strict';
describe("DateUtil Service test", function() {
	var DateUtils;

	beforeEach(angular.mock.module('app.util'));

	beforeEach(inject(function(_DateUtil_) {
		DateUtils = _DateUtil_;
	}));

	describe("add days", function() {
		function expectEquals(actual, expected) {
			expect(actual.getTime()).toBeGreaterThanOrEqual(expected.getTime());
			expect(actual.getTime()).toBeLessThanOrEqual(expected.getTime() + 1000);
		}

		it("should return the current date if the passed is undefined or null", function() {
			var now = new Date();
			expectEquals(DateUtils.addDays(undefined, 10), now);
		});

		it("should return the current date if the passed days are not numbers", function() {
			var now = new Date();
			var date = new Date(2020, 10, 10);

			expectEquals(DateUtils.addDays(date, undefined), now);
			expectEquals(DateUtils.addDays(date, null), now);
			expectEquals(DateUtils.addDays(date, ""), now);
		});

		it("should add days to the date", function() {
			var date = new Date(2020, 10, 10);
			expectEquals(DateUtils.addDays(date, 10), new Date(date.getTime() + 10 * 24 * 60 * 60 * 1000));
		});
	});

	describe("equals", function() {
		it("should return false when the passed dates are undefined or null", function() {
			var date = new Date();
			expect(DateUtils.equals(undefined, date)).toBeFalsy();
			expect(DateUtils.equals(null, date)).toBeFalsy();
			expect(DateUtils.equals(date, undefined)).toBeFalsy();
			expect(DateUtils.equals(date, undefined)).toBeFalsy();
		});

		it("shoud return true when the dates are equal", function() {
			var date1 = new Date(2020, 10, 10);
			var date2 = new Date(2020, 10, 10);
			expect(DateUtils.equals(date1, date2)).toBeTruthy();
		});

		it("shoud return false when the dates are not equal", function() {
			var date1 = new Date(2020, 10, 10);
			var date2 = new Date(2017, 10, 10);
			expect(DateUtils.equals(date1, date2)).toBeFalsy();
		});
	});
});
