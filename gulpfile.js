/**
 * @author Borche Petrovski
 */

var gulp = require('gulp'),
	concat = require('gulp-concat'),
	rev = require('gulp-rev-append'),
	uglify = require('gulp-uglify'),
	cssNano = require('gulp-cssnano'),
	templateCache = require('gulp-angular-templatecache'),
	htmlmin = require('gulp-htmlmin'),
	filter = require('gulp-filter'),
	angularFilesort = require('gulp-angular-filesort'),
	del = require('del'),
	gulpif = require('gulp-if'),
	browserSync = require('browser-sync'),
	runSequence = require('run-sequence'),
	bowerFiles = require('main-bower-files'),
	url = require('url'),
	fs = require('fs'),
	proxy = require('proxy-middleware'),
	gutil = require('gulp-util'),
	eslint = require('gulp-eslint'),
	path = require('path');

var config = {
	app: 'src/main/webapp/',
	dist: 'src/main/webapp/dist/',
	bower: 'src/main/webapp/assets/libs/',
	assets: 'src/main/webapp/assets/',
	eslintReport: 'src/main/webapp/reports/',
	module: 'app',
	uri: 'http://localhost:',
	port: 9000,
	apiUrl: 'http://localhost::8000',
	proxyRoutes: [
		'/api'
	]
};

/*
 |----------------------------------------------------------------
 | Util functions
 |----------------------------------------------------------------
 */

function isProduction() {
	return gutil.env.env === 'prod';
}

function isLintFixed(file) {
	// Has ESLint fixed the file contents?
	return file.eslint !== null && file.eslint.fixed;
}

/*
 |----------------------------------------------------------------
 | Gulp tasks
 |----------------------------------------------------------------
 */

gulp.task('build-lib-js', function() {
	return gulp.src(bowerFiles())
			.pipe(filter('**/*.js'))
			.pipe(concat('lib.js'))
			.pipe(gulpif(isProduction(), uglify()))
			.pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('build-lib-css', function() {
	return gulp.src(bowerFiles())
			.pipe(filter('**/*.css'))
			.pipe(concat('lib.css'))
			.pipe(gulpif(isProduction(), cssNano()))
			.pipe(gulp.dest(config.dist + 'css'));
});

gulp.task('copy-fonts', function() {
	var src = [
		config.bower + 'bootstrap/fonts/**.{ttf,woff,eot,svg,woff2}',
		config.bower + 'font-awesome/fonts/*.{ttf,woff,eot,svg,woff2}'
	];
	return gulp.src(src)
			.pipe(gulp.dest(config.dist + 'fonts'));
});

gulp.task('copy-images', function() {
	return gulp.src(config.assets + 'images/**/*')
			.pipe(gulp.dest(config.dist + 'images'));

});

gulp.task('build-custom-css', function() {
	return gulp.src(config.assets + "css/**/*.css")
			.pipe(concat('custom.css'))
			.pipe(gulpif(isProduction, cssNano()))
			.pipe(gulp.dest(config.dist + 'css'));
});

gulp.task('build-app-js', function() {
	return gulp.src(config.app + 'app/**/*.js')
			.pipe(angularFilesort())
			.pipe(concat('app.js'))
			.pipe(gulpif(isProduction(), uglify()))
			.pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('html-template-cache', function() {
	return gulp.src(config.app + 'app/**/*.html')
			.pipe(htmlmin({collapseWhitespace: true}))
			.pipe(templateCache('templates.js', {
				module: config.module,
				root: 'app'
			}))
			.pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('cache-break', function() {
	return gulp.src(config.app + 'index.html')
			.pipe(rev())
			.pipe(gulp.dest(config.app));
});

gulp.task('clean', function() {
	return del([config.dist], {dot: true});
});

gulp.task('build', ['clean'], function(callback) {
	runSequence(['build-lib-js', 'build-lib-css', 'copy-fonts', 'copy-images','build-custom-css',
				 'build-app-js', 'html-template-cache'],'cache-break', callback);
});

gulp.task('install', function(callback) {
	runSequence(['build-lib-js', 'build-lib-css', 'copy-fonts'], 'cache-break', callback);
});

gulp.task('watch', function() {
	gulp.watch('bower.json', ['install']);
	gulp.watch(config.assets + 'css/**/*.css', ['build-custom-css', 'cache-break']);
	gulp.watch(config.app + 'app/**/*.js', ['build-app-js', 'cache-break']);
	gulp.watch(config.app + 'app/**/*.html', ['html-template-cache', 'cache-break']);
	gulp.watch([config.app + 'app/**', config.assets + 'css/**'])
		.on('change', browserSync.reload);
});

gulp.task('serve', ['build', 'watch'], function() {
	var baseUri = config.apiUrl;
	var proxies = config.proxyRoutes.map(function(r) {
		var options = url.parse(baseUri + r);
		options.route = r;
		return proxy(options);
	});

	browserSync({
		open: true,
		port: config.port,
		server: {
			baseDir: config.app,
			middleware: proxies
		}
	});
});

gulp.task('default', ['build']);

gulp.task('eslint', function() {
	return gulp.src(config.app + 'app/**/*.js')
			.pipe(eslint())
			.pipe(eslint.format());
});

gulp.task('eslint:html', function() {
	return gulp.src(config.app + 'app/**/*.js')
			.pipe(eslint())
			.pipe(eslint.format('html', function(output) {
				var dir = path.join(__dirname, config.eslintReport);
				if (!fs.existsSync(dir)) {
				    fs.mkdirSync(dir);
				}
				fs.writeFileSync(dir + '/eslint.html', output);
			}));
});

gulp.task('eslint:fix', function() {
	return gulp.src(config.app + 'app/**/*.js')
			.pipe(eslint({
				fix: true
			}))
			.pipe(eslint.format())
			.pipe(gulpif(isLintFixed, gulp.dest(config.app + 'app')));
});