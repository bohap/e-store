'use strict';
var gutil = require('gulp-util');

module.exports = {
	isProduction: isProduction,
	notificationEnabled: notificationEnabled,
	isLintFixed: isLintFixed
};

/**
 * Checks of the current mode is production.
 */
function isProduction() {
	return gutil.env.production !== undefined || gutil.env.prod !== undefined;
}

/**
 * Checks if the notification are enabled.
 */
function notificationEnabled() {
	return gutil.env.notification === undefined ? true : gutil.end.notification;
}

/**
 * Checks if the lint errors in the file are fixst.
 */
function isLintFixed(file) {
	return file.eslint !== null && file.eslint.fixed;
}
