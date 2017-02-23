'use strict';
var gulp = require('gulp');
var inject = require('gulp-inject');
var bowerFiles = require('main-bower-files');
var config = require('./config');
var angularFilesort = require('gulp-angular-filesort');

module.exports = {
	test: test
};

/**
 * Add the paths of the boewr files and the angular modules to the karma configuration file.
 */
function test() {
	var bower = gulp.src(bowerFiles({ includeDev: true, filter: ['**/*.js'] }), { read: false });
	var modules = gulp.src(config.app + 'app/**/*.module.js').pipe(angularFilesort());

	return gulp.src('karma.conf.js')
		.pipe(inject(bower, {
			starttag: '// bower:js',
			endtag: '// endbower',
			transform: getFilepath
		}))
		.pipe(inject(modules, {
			starttag: '// angular:modules',
			endtag: '// endangular',
			transform: getFilepath
		}))
		.pipe(gulp.dest('./'));

	function getFilepath(filepath) {
		return '\'' + filepath.substring(1, filepath.length) + '\',';
	}
}
