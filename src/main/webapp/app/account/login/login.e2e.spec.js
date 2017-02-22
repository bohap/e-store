'use strict';
describe("Login E2E Test", function() {
	var username = element(by.id("email"));
	var password = element(by.id("password"));
	var submit = element(by.css("button[type=submit]"));
	var usernameErrors = element(by.id('username-errors')).all(by.tagName('strong'));
	var passwordErrors = element(by.id('password-errors')).all(by.tagName('strong'));

	beforeAll(function() {
		browser.get("/");
	});

	it("should display error message when the email or the password are invalid", function() {
		element(by.id('login')).click();

		// Username Tests
		expect(element(by.id('username-errors')).isDisplayed()).toBeFalsy();

		// Test the username field for a empty string
		username.sendKeys();
		element(by.css("label[for=email]")).click();
		expect(element(by.id('username-errors')).isDisplayed()).toBeTruthy();
		expect(usernameErrors.count()).toEqual(1);
		expect(usernameErrors.first().getText()).toMatch(/required/);

		// Test the username field for a invalid email
		username.sendKeys("test");
		element(by.css("label[for=email]")).click();
		expect(usernameErrors.count()).toEqual(1);
		expect(usernameErrors.first().getText()).toMatch(/not a valid email/);

		// Password Tests
		expect(element(by.id("password-errors")).isDisplayed()).toBeFalsy();

		// Test password field for a empty string
		password.sendKeys();
		element(by.css("label[for=password]")).click();
		expect(element(by.id("password-errors")).isDisplayed()).toBeTruthy();
		expect(passwordErrors.count()).toEqual(1);
		expect(passwordErrors.first().getText()).toMatch(/required/);

		// Test the password filed for a less then six characters text
		password.sendKeys("abcde");
		element(by.css("label[for=password]")).click();
		expect(passwordErrors.count()).toEqual(1);
		expect(passwordErrors.first().getText()).toMatch(/must be at least six characters/);

		element(by.className('modal')).element(by.className('close')).click();
	});

	it("should display error message when the credentials are not valid", function() {
		element(by.id('login')).click();

		var errors = element(by.tagName("errors-list")).element(by.className('alert'));
		expect(errors.isPresent()).toBeFalsy();

		username.sendKeys("test@test.com");
		password.sendKeys("password");
		submit.click();

		expect(errors.isPresent()).toBeTruthy();

		element(by.className('modal')).element(by.className('close')).click();
	});

	it("should login successfully with admin account", function() {
		element(by.id('login')).click();

		var errors = element(by.tagName("errors-list")).element(by.className('alert'));

		username.sendKeys("admin@book-store.com");
		password.sendKeys("password");
		submit.click();

		expect(errors.isPresent()).toBeFalsy();
		expect(element(by.id("identity")).getText()).toEqual("admin");

		// Logout the user
		element(by.id("identity")).click();
		element(by.id("logout")).click();
	});
});
