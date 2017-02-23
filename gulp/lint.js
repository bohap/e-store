'use strict';
var gulp = require('gulp');
var gulpIf = require('gulp-if');
var eslint = require('gulp-eslint');
var fs = require('fs');
var mkdirp = require('mkdirp');
var path = require('path');
var gutil = require('gulp-util');
var config = require('./config');
var util = require('./util');

module.exports = {
	eslint: lint,
	eslintHtmlReport: eslintHtmlReport,
	eslintFix: eslintFix
};

/**
 * Check if the app files contains some errors,
 */
function lint() {
	return gulp.src(config.app + 'app/**/*.js')
		.pipe(eslint())
		.pipe(eslint.format());
}

/**
 * Generate a html report for the errors in the app files.
 */
function eslintHtmlReport() {
	return gulp.src(config.app + 'app/**/*.js')
		.pipe(eslint())
		.pipe(eslint.format('html', function(output) {
			var dir = path.join(__dirname, '..', config.eslintReport);
			mkdirp(dir, function(err) {
				if (err) {
					gutil.log(err);
					process.exit(1);
				} else {
					fs.writeFileSync(dir + '/report.html', output);
				}
			});
		}));
}

/**
 * Try to fix the errors in the app files.
 */
function eslintFix() {
	return gulp.src(config.app + 'app/**/*.js')
		.pipe(eslint({ fix: true }))
		.pipe(eslint.format())
		.pipe(gulpIf(util.isLintFixed, gulp.dest(config.app + 'app')));
}
