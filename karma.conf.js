// Karma configuration

module.exports = function(config) {
	config.set({

		// base path that will be used to resolve all patterns (eg. files, exclude)
		basePath: './',


		// frameworks to use
		// available frameworks: https://npmjs.org/browse/keyword/karma-adapter
		frameworks: ['jasmine'],


		// list of files / patterns to load in the browser
		files: [
			// bower:js
			'src/main/webapp/assets/libs/jquery/dist/jquery.js',
			'src/main/webapp/assets/libs/angular/angular.js',
			'src/main/webapp/assets/libs/angular-component-router/angular_1_router.js',
			'src/main/webapp/assets/libs/angular-component-router/ng_route_shim.js',
			'src/main/webapp/assets/libs/angular-resource/angular-resource.js',
			'src/main/webapp/assets/libs/angular-loading-bar/build/loading-bar.js',
			'src/main/webapp/assets/libs/angular-animate/angular-animate.js',
			'src/main/webapp/assets/libs/angular-bootstrap/ui-bootstrap-tpls.js',
			'src/main/webapp/assets/libs/angular-toastr/dist/angular-toastr.tpls.js',
			'src/main/webapp/assets/libs/ngstorage/ngStorage.js',
			'src/main/webapp/assets/libs/ngInfiniteScroll/build/ng-infinite-scroll.js',
			'src/main/webapp/assets/libs/angular-messages/angular-messages.js',
			'src/main/webapp/assets/libs/angular-ui-select/dist/select.js',
			'src/main/webapp/assets/libs/angular-smart-table/dist/smart-table.js',
			'src/main/webapp/assets/libs/moment/moment.js',
			'src/main/webapp/assets/libs/angular-moment/angular-moment.js',
			// endbower
			// angular:modules
			'src/main/webapp/app/account/account.module.js',
			'src/main/webapp/app/app.module.js',
			// endangular
			'src/main/webapp/app/**/*!(.module|.spec|.e2e).js',
			'src/main/webapp/app/**/*.spec.js',
		],


		// list of files to exclude
		exclude: [
		],


		// preprocess matching files before serving them to the browser
		// available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
		preprocessors: {
			'./app/**': ['coverage']
		},


		// test results reporter to use
		// possible values: 'dots', 'progress'
		// available reporters: https://npmjs.org/browse/keyword/karma-reporter
		reporters: ['dots', 'junit', 'coverage', 'progress'],

		junitReporter: {
			outputFile: '../build/reports/karma/test-result.xml'
		},

		coverageReporter: {
			dir: 'build/reports/coverage',
			reporters: [
				{ type: 'lcov', subdir: 'report-lcov' }
			]
		},


		// web server port
		port: 9876,


		// enable / disable colors in the output (reporters and logs)
		colors: true,


		// level of logging
		// possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
		logLevel: config.LOG_INFO,


		// enable / disable watching file and executing tests whenever any file changes
		autoWatch: false,

		customLaunchers: {
			Chrome_with_debugging: {
				base: 'Chrome',
				flags: ['--remote-debugging-port=9222']
			}
		},

		// start these browsers
		// available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
		browsers: ['PhantomJS'],


		// Continuous Integration mode
		// if true, Karma captures browsers, runs the tests and exits
		singleRun: false,

		// to avoid DISCONNECTED messages when connecting to slow virtual machines
		browserDisconnectTimeout: 10000, // default 2000
		browserDisconnectTolerance: 1, // default 0
		browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
	});
};
