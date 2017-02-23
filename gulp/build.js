'use strict';
var gulp = require('gulp');
var lazypipe = require('lazypipe');
var gulpIf = require('gulp-if');
var plumber = require('gulp-plumber');
var sourcemaps = require('gulp-sourcemaps');
var bowerFiles = require('main-bower-files');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var cssNano = require('gulp-cssnano');
var angularFilesort = require('gulp-angular-filesort');
var htmlmin = require('gulp-htmlmin');
var templateCache = require('gulp-angular-templatecache');
var rev = require('gulp-rev-append');

var handleErrors = require('./handle-errors');
var config = require('./config');
var util = require('./util');

module.exports = {
	vendor: vendor,
	app: app,
	css: css,
	html: html,
	cacheBreak: cacheBreak
};

/**
 * Concat the bower libs.
 */
function vendor() {
	var jsChannel = lazypipe()
		.pipe(concat, 'lib.js')
		.pipe(function() {
			return gulpIf(util.isProduction(), uglify());
		})
		.pipe(gulp.dest, config.dist + 'js');

	var cssChannel = lazypipe()
		.pipe(concat, 'lib.css')
		.pipe(function() {
			return gulpIf(util.isProduction(), cssNano());
		})
		.pipe(gulp.dest, config.dist + 'css');

	return gulp.src(bowerFiles())
		.pipe(plumber({ errorHandler: handleErrors }))
		.pipe(gulpIf('*.js', jsChannel()))
		.pipe(gulpIf('*.css', cssChannel()));
}

/**
 * Concat the app js files.
 */
function app() {
	return gulp.src([config.app + 'app/**/*.js', '!' + config.app + 'app/**/*.spec.js'])
		.pipe(plumber({ errorHandler: handleErrors }))
		.pipe(angularFilesort())
		.pipe(sourcemaps.init())
		.pipe(concat('app.js'))
		.pipe(gulpIf(util.isProduction(), uglify()))
		.pipe(sourcemaps.write("."))
		.pipe(gulp.dest(config.dist + 'js'));
}

/**
 * Concat the custom css files.
 */
function css() {
	return gulp.src(config.assets + "css/**/*.css")
		.pipe(plumber({ errorHandler: handleErrors }))
		.pipe(concat('custom.css'))
		.pipe(gulpIf(util.isProduction(), cssNano()))
		.pipe(gulp.dest(config.dist + 'css'));
}

/**
 * Put the partial html files into the angular template cache.
 */
function html() {
	return gulp.src(config.app + 'app/**/*.html')
		.pipe(plumber({ errorHandler: handleErrors }))
		.pipe(htmlmin({ collapseWhitespace: true }))
		.pipe(templateCache('templates.js', {
			module: config.module,
			root: 'app'
		}))
		.pipe(gulp.dest(config.dist + 'js'));
}

/**
 * Add a revision to the resources in the base index.html file to prevent caching the resources.
 */
function cacheBreak() {
	return gulp.src(config.app + 'index.html')
		.pipe(rev())
		.pipe(gulp.dest(config.app));
}
