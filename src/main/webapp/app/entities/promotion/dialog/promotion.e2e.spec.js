'use strict';
describe("Promotion E2E Tests", function() {

	beforeAll(function() {
		browser.get("/");
	});

	it("it should create a new promotion", function() {
		loginUser();
		browser.waitForAngular();
		browser.get("dashboard/books");

		var button = element.all(by.className('add-promotion')).first();
		button.click();

		element(by.id("newPrice")).sendKeys(1234);

		// Choose a start date
		element(by.id("start-btn")).click();
		element(by.id("promotion-start-date")).all(by.className("uib-day")).filter(function(el) {
			return el.element(by.tagName("button")).getAttribute("disabled").then(function(attr) {
				return attr === null;
			});
		}).then(function(filtered) {
			filtered[0].element(by.tagName("button")).click();
		});

		// Choose a end date
		element(by.id("end-btn")).click();
		element(by.id("promotion-end-date")).all(by.className("uib-day")).filter(function(el) {
			return el.element(by.tagName("button")).getAttribute("disabled").then(function(attr) {
				return attr === null;
			});
		}).then(function(filtered) {
			filtered[0].element(by.tagName("button")).click();
		});

		element(by.css("button[type=submit]")).click();

		var toastr = element(by.className('toast')).element(by.className('toast-message'));
		expect(toastr.getText()).toMatch(/successfully/);

		element(by.className('toast-close-button')).click();
		element(by.id("identity")).click();
		element(by.id("logout")).click();
	});

	function loginUser() {
		element(by.id('login')).click();
		element(by.id("email")).sendKeys("admin@book-store.com");
		element(by.id("password")).sendKeys("password");
		element(by.css("button[type=submit]")).click();
	}
});
