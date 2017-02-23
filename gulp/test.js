'use strict';
var gulp = require('gulp');
var path = require('path');
var gutil = require('gulp-util');
var KarmaServer = require('karma').Server;
var Protractor = require('gulp-protractor').protractor;

module.exports = {
	karma: karma,
	protractor: protractor
};

/**
 * Start a karma server for running unit tests.
 */
function karma(done) {
	var file = path.join(__dirname, '..', 'karma.conf.js');
	return new KarmaServer({
		configFile: file,
		singleRun: true
	}, done).start();
}

/**
 * Configure the protractor for running the E2E tests.
 */
function protractor() {
	var configObj = {
		configFile: './protractor.conf.js'
	};
	if (gutil.env.suite) {
		configObj['args'] = ['--suite', gutil.env.suite];
	}

	return gulp.src([])
		.pipe(Protractor(configObj))
		.on('error', function() {
			gutil.log('E2E Tests failed');
			process.exit(1);
		});
}
