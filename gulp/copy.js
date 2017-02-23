'use-strct';
var gulp = require('gulp');
var config = require('./config');

module.exports = {
	images: images,
	fonts: fonts
};

/**
 * Copy the image to the destionation directory.
 */
function images() {
	return gulp.src(config.assets + 'images/**/*')
		.pipe(gulp.dest(config.dist + 'images'));
}

/**
 * Copy the fonts to the destination directory.
 */
function fonts() {
	var src = [
		config.bower + 'bootstrap/fonts/**.{ttf,woff,eot,svg,woff2}',
		config.bower + 'font-awesome/fonts/*.{ttf,woff,eot,svg,woff2}'
	];
	return gulp.src(src)
		.pipe(gulp.dest(config.dist + 'fonts'));
}
