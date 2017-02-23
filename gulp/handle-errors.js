'use strict';
var notify = require('gulp-notify');
var util = require('./util');

/**
 * Show a error message when some error in the pipes occurred.
 */
module.exports = function() {
	if (util.notificationEnabled()) {
		var args = Array.prototype.slice.call(arguments);
		notify.onError({
			title: "JHipster Gulp Build",
			subtitle: "Failure!",
			message: "Error: <%= error.message %>",
			sound: "Beep"
		}).apply(this, args);
	}
};
