'use strict';
describe("Book Create E2E Tests", function() {
	var path = require('path');

	beforeAll(function() {
		browser.get("/");
	});

	it("should redirect if the logged user is not admin", function() {
		loginUser("test@user.com");
		browser.waitForAngular();
		browser.get("/books/create");
		expect(element(by.id("add-book")).isPresent()).toBeFalsy();
		expect(browser.getCurrentUrl()).not.toMatch(/books\/create$/);
		var toastr = element(by.className('toast')).element(by.className('toast-message'));
		expect(toastr.getText()).toMatch(/don't have permissions/);

		element(by.className('toast-close-button')).click();
		element(by.id("identity")).click();
		element(by.id("logout")).click();
	});

	it("should create a book successfully", function() {
		loginUser("admin@book-store.com");
		element(by.id("add-book")).click();

		element(by.id("name")).sendKeys("Test Book");
		element(by.id("shortDescription")).sendKeys(new Array(10).join("test"));
		element(by.id("body")).sendKeys(new Array(20).join("test "));
		element(by.id("price")).sendKeys(123);
		element(by.id("publisher")).sendKeys("Test Publisher");
		element(by.id("author")).sendKeys("Test Author");
		element(by.id("year")).sendKeys(2017);
		element(by.id("pages")).sendKeys(1234);

		// Add a image absolute path
		var imagePath = "webapp/app/entities/book".replace(/[^/]+/g, '..') + "/resources/img/book.png";
		var imageAbsoulePath = path.resolve(__dirname, imagePath);
		element(by.id("image")).sendKeys(imageAbsoulePath);

		// Select a categories
		var selectButton = element(by.id("categories"));
		var selectInput = selectButton.element(by.css('.ui-select-search'));
		selectButton.click();
		selectInput.sendKeys("development");
		element.all(by.css('.ui-select-choices-row-inner span')).first().click();
		selectButton.click();
		selectInput.sendKeys("test");
		element.all(by.css('.ui-select-choices-row-inner span')).first().click();

		element(by.css("button[type=submit]")).click();

		expect(browser.getCurrentUrl()).toMatch(/books\/test-book/);

		element(by.id("identity")).click();
		element(by.id("logout")).click();
	});

	function loginUser(username) {
		element(by.id('login')).click();
		element(by.id("email")).sendKeys(username);
		element(by.id("password")).sendKeys("password");
		element(by.css("button[type=submit]")).click();
	}
});
