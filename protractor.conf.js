var os = require('os');
var ngStorageMock = require('ngstorage-mock');

var HtmlScreenshotReporter = require("protractor-jasmine2-screenshot-reporter");
var JasmineReporters = require('jasmine-reporters');

var webbrowserDriver = '';
if (os.platform() === 'win32') {
	webbrowserDriver = 'node_modules/protractor/node_modules/webdriver-manager/selenium/chromedriver_2.27.exe';
} else {
	webbrowserDriver = 'node_modules/protractor/node_modules/webdriver-manager/selenium/chromedriver_2.27';
}

var htmlScreenshotReporter = new HtmlScreenshotReporter({
	dest: 'build/reports/e2e/screenshot/',
	captureOnlyFailedSpecs: false,
});

exports.config = {
	seleniumServerJar: 'node_modules/protractor/node_modules/webdriver-manager/selenium/selenium-server-standalone-3.0.1.jar',
	chromeDriver: webbrowserDriver,
	allScriptsTimeout: 20000,

	suites: {
		account: "src/main/webapp/app/account/**/*.e2e.spec.js",
		book: "src/main/webapp/app/entities/book/**/*.e2e.spec.js",
		dashboard: "src/main/webapp/app/entities/dashboard/**/*.e2e.spec.js",
		promotion: "src/main/webapp/app/entities/promotion/**/*.e2e.spec.js",
	},

	capabilities: {
		'browserName': 'chrome',
		'phantomjs.binary.path': require('phantomjs-prebuilt').path,
		'phantomjs.ghostdriver.cli.args': ['--loglevel=DEBUG']
	},

	directConnect: true,

	baseUrl: 'http://localhost:9000/',

	framework: 'jasmine2',

	jasmineNodeOpts: {
		showColors: true,
		defaultTimeoutInterval: 30000
	},

	beforeLaunch: function() {
		return new Promise(function(resolve) {
			htmlScreenshotReporter.beforeLaunch(resolve);
		});
	},

	onPrepare: function() {
		// Disable animations so e2e tests run more quickly
		var disableNgAnimate = function() {
			angular
				.module('disableNgAnimate', [])
				.run(['$animate', function($animate) {
					$animate.enabled(false);
				}]);
		};

		var disableCssAnimate = function() {
			angular
				.module('disableCssAnimate', [])
				.run(function() {
					var style = document.createElement('style');
					style.type = 'text/css';
					style.innerHTML = 'body * {' +
						'-webkit-transition: none !important;' +
						'-moz-transition: none !important;' +
						'-o-transition: none !important;' +
						'-ms-transition: none !important;' +
						'transition: none !important;' +
						'}';
					document.getElementsByTagName('head')[0].appendChild(style);
				});
		};

		browser.addMockModule('disableNgAnimate', disableNgAnimate);
		browser.addMockModule('disableCssAnimate', disableCssAnimate);
		browser.addMockModule('ngStorage', ngStorageMock);

		browser.driver.manage().window().setSize(1280, 1024);
		jasmine.getEnv().addReporter(new JasmineReporters.JUnitXmlReporter({
			savePath: 'build/reports/e2e',
			consolidateAll: false
		}));
		jasmine.getEnv().addReporter(htmlScreenshotReporter);
	},

	afterLaunch: function(exitCode) {
		return new Promise(function(resolve) {
			htmlScreenshotReporter.afterLaunch(resolve.bind(this, exitCode));
		});
	}
};
