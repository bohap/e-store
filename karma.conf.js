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
			'src/main/webapp/assets/libs/angular-mocks/angular-mocks.js',
			// endbower
			// angular:modules
			'src/main/webapp/app/entities/user/service/user.service.module.js',
			'src/main/webapp/app/entities/promotion/service/promotion.service.module.js',
			'src/main/webapp/app/entities/promotion/dialog/promotion.dialog.module.js',
			'src/main/webapp/app/entities/order/service/order.service.module.js',
			'src/main/webapp/app/entities/order/dialog/order.dialog.module.js',
			'src/main/webapp/app/entities/order/_partials/order.partial.module.js',
			'src/main/webapp/app/entities/favorite/service/favorite.service.module.js',
			'src/main/webapp/app/entities/category/service/category.service.module.js',
			'src/main/webapp/app/entities/book/service/book.service.module.js',
			'src/main/webapp/app/entities/book/directives/book.directive.module.js',
			'src/main/webapp/app/entities/book/_partials/book.partial.module.js',
			'src/main/webapp/app/entities/basket/service/basket.service.module.js',
			'src/main/webapp/app/layouts/navbar/nav.module.js',
			'src/main/webapp/app/layouts/error/error.module.js',
			'src/main/webapp/app/layouts/dialog/dialog.module.js',
			'src/main/webapp/app/entities/user/user.module.js',
			'src/main/webapp/app/entities/order/order.module.js',
			'src/main/webapp/app/entities/dashboard/dashboard.module.js',
			'src/main/webapp/app/entities/book/book.module.js',
			'src/main/webapp/app/entities/basket/basket.module.js',
			'src/main/webapp/app/blocks/interceptor/interceptor.module.js',
			'src/main/webapp/app/blocks/handler/handler.module.js',
			'src/main/webapp/app/blocks/constants/constans.module.js',
			'src/main/webapp/app/components/util/util.module.js',
			'src/main/webapp/app/components/notifiy/notify.module.js',
			'src/main/webapp/app/components/key-events/key-event.module.js',
			'src/main/webapp/app/components/html/html.module.js',
			'src/main/webapp/app/components/form-validate/validate.module.js',
			'src/main/webapp/app/home/home.module.js',
			'src/main/webapp/app/account/auth.module.js',
			'src/main/webapp/app/account/account.module.js',
			'src/main/webapp/app/app.module.js',
			'src/main/webapp/app/app.core.module.js',
			// endangular
			'src/main/webapp/app/**/*!(.module|.spec).js',
			'src/main/webapp/app/**/*.spec.js',
		],


		// list of files to exclude
		exclude: [
			'src/main/webapp/app/**/*.e2e.spec.js',
		],


		// preprocess matching files before serving them to the browser
		// available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
		preprocessors: {
			'src/main/webapp/app/**/!(*spec|*e2e.spec).js': ['coverage']
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
