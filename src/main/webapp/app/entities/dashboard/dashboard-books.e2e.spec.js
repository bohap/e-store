'use strict';
describe("Dashboard Book E2E Tests", function() {

	beforeAll(function() {
		browser.get("/");
	});

	it("should remove book", function() {
		loginUser("admin@book-store.com");
		browser.waitForAngular();
		browser.get("/dashboard/books");

		element.all(by.repeater("book in books")).count()
			.then(onElementsCounted);

		function onElementsCounted(count) {
			element.all(by.className("remove-book-btn")).first().click();
			element(by.id("confirm-dialog-yes")).click();

			element(by.id("identity")).click();
			element(by.id("logout")).click();
		}
	});

	function loginUser(username) {
		element(by.id('login')).click();
		element(by.id("email")).sendKeys(username);
		element(by.id("password")).sendKeys("password");
		element(by.css("button[type=submit]")).click();
	}
});
