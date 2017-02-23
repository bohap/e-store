/**
 * @author Borche Petrovski
 */
var gulp = require('gulp');
var del = require('del');
var browserSync = require('browser-sync');
var runSequence = require('run-sequence');

var config = require('./gulp/config.js');
var build = require('./gulp/build.js');
var copy = require('./gulp/copy.js');
var inject = require('./gulp/inject.js');
var lint = require('./gulp/lint.js');
var serve = require('./gulp/serve.js');
var test = require('./gulp/test.js');

gulp.task('vendor', build.vendor);
gulp.task('app', build.app);
gulp.task('css', build.css);
gulp.task('html', build.html);
gulp.task('copy-fonts', copy.fonts);
gulp.task('copy-images', copy.images);
gulp.task('cache-break', build.cacheBreak);

gulp.task('clean', function() {
	return del([config.dist], { dot: true });
});

gulp.task('reload', function(cb) {
	browserSync.reload();
	cb();
});

gulp.task('build', ['clean'], function(callback) {
	runSequence(['vendor', 'app', 'css', 'html', 'copy-fonts', 'copy-images'], 'cache-break', callback);
});

gulp.task('watch', function() {
	gulp.watch('bower.json', function() {
		runSequence(['vendor', 'copy-fonts'], 'cache-break', 'reload');
	});

	gulp.watch(config.assets + 'css/**/*.css', function() {
		runSequence('css', 'cache-break', 'reload');
	});

	gulp.watch(config.app + 'app/**/*.js', function() {
		runSequence('app', 'cache-break', 'reload');
	});

	gulp.watch(config.app + 'app/**/*.html', function() {
		runSequence('html', 'cache-break', 'reload');
	});
});

gulp.task('serve', ['build', 'watch'], serve);

gulp.task('test:inject', inject.test);
gulp.task('test', ['test:inject'], test.karma);

gulp.task('itest', test.protractor);

gulp.task('eslint', lint.eslint);
gulp.task('eslint:html', lint.eslintHtmlReport);
gulp.task('eslint:fix', lint.eslintFix);

gulp.task('default', ['build']);
