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
	path = require('path'),
	historyApiFallback = require('connect-history-api-fallback'),
	plumber = require('gulp-plumber'),
	notify = require('gulp-notify'),
	sourcemaps = require('gulp-sourcemaps'),
	inject = require('gulp-inject'),
	KarmaServer = require('karma').Server,
	protractor = require('gulp-protractor').protractor;

var config = {
	app: 'src/main/webapp/',
	dist: 'src/main/webapp/dist/',
	bower: 'src/main/webapp/assets/libs/',
	assets: 'src/main/webapp/assets/',
	eslintReport: 'src/main/webapp/reports/',
	module: 'app',
	uri: 'http://localhost:',
	port: 9000,
	apiUrl: 'http://localhost:8080',
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

function notificationEnabled() {
	return gutil.env.notification === undefined ? true : gutil.end.notification;
}

function isLintFixed(file) {
	// Has ESLint fixed the file contents?
	return file.eslint !== null && file.eslint.fixed;
}

/**
 * Notify the user that a error has happened
 */
function handleErrors() {
	if (notificationEnabled()) {
		var args = Array.prototype.slice.call(arguments);
		notify.onError({
			title: "JHipster Gulp Build",
			subtitle: "Failure!",
			message: "Error: <%= error.message %>",
			sound: "Beep"
		}).apply(this, args);
	}
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
	return gulp.src([config.app + 'app/**/*.js', '!' + config.app + 'app/**/*.{spec,e2e}.js'])
		.pipe(plumber({ errorHandler: handleErrors }))
		.pipe(angularFilesort())
		.pipe(sourcemaps.init())
		.pipe(concat('app.js'))
		.pipe(gulpif(isProduction(), uglify()))
		.pipe(sourcemaps.write("."))
		.pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('html-template-cache', function() {
	return gulp.src(config.app + 'app/**/*.html')
		.pipe(htmlmin({ collapseWhitespace: true }))
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
	return del([config.dist], { dot: true });
});

gulp.task('reload', function(cb) {
	browserSync.reload();
	cb();
});

gulp.task('build', ['clean'], function(callback) {
	runSequence(['build-lib-js', 'build-lib-css', 'copy-fonts', 'copy-images', 'build-custom-css',
		'build-app-js', 'html-template-cache'], 'cache-break', callback);
});

gulp.task('install', function(callback) {
	runSequence(['build-lib-js', 'build-lib-css', 'copy-fonts'], 'cache-break', 'reload', callback);
});

gulp.task('watch', function() {
	gulp.watch('bower.json', ['install']);
	gulp.watch(config.assets + 'css/**/*.css', function() {
		runSequence('build-custom-css', 'cache-break', 'reload');
	});
	gulp.watch(config.app + 'app/**/*.js', function() {
		runSequence('build-app-js', 'cache-break', 'reload');
	});
	gulp.watch(config.app + 'app/**/*.html', function() {
		runSequence('html-template-cache', 'cache-break', 'reload');
	});
});

gulp.task('serve', ['build', 'watch'], function() {
	var baseUri = config.apiUrl;
	var proxies = config.proxyRoutes.map(function(r) {
		var options = url.parse(baseUri + r);
		options.route = r;
		return proxy(options);
	});
	proxies.push(historyApiFallback());

	browserSync({
		open: true,
		port: config.port,
		server: {
			baseDir: config.app,
			middleware: proxies
		}
	});
});

gulp.task('test:inject', function() {
	// inject the bower resource files in the karma config
	var bower = gulp.src(bowerFiles({ includeDev: true, filter: ['**/*.js'] }), { read: false });
	gulp.src('karma.conf.js')
		.pipe(inject(bower, {
			starttag: '// bower:js',
			endtag: '// endbower',
			transform: function(filepath) {
				return '\'' + filepath.substring(1, filepath.length) + '\',';
			}
		}))
		.pipe(gulp.dest('./'));

	// inject all the angular modules in the application so that they will be loaded first
	var modules = gulp.src(config.app + 'app/**/*.module.js')
							.pipe(angularFilesort());
	gulp.src('karma.conf.js')
		.pipe(inject(modules, {
			starttag: '// angular:modules',
			endtag: '// endangular',
			transform: function(filepath) {
				return '\'' + filepath.substring(1, filepath.length) + '\',';
			}
		}))
		.pipe(gulp.dest('./'));
});

gulp.task('test', ['test:inject'], function(done) {
	new KarmaServer({
		configFile: __dirname + '/karma.conf.js',
		singleRun: true
	}, done).start();
});

gulp.task('itest', function() {
	var configObj = {
		configFile: './protractor.conf.js'
	};
	if (gutil.env.suite) {
		configObj['args'] = ['--suite', gutil.env.suite];
	}

	return gulp.src([])
		.pipe(protractor(configObj))
		.on('error', function() {
			gutil.log('E2E Tests failed');
			process.exit(1);
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
