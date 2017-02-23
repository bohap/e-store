'use strict';
var url = require('url');
var proxy = require('proxy-middleware');
var historyApiFallback = require('connect-history-api-fallback');
var browserSync = require('browser-sync');
var config = require('./config');

/**
 * Start a new local serve and set the index.html as the entry point.
 */
module.exports = function() {
	var baseUri = config.apiUrl;
	var proxies = config.proxyRoutes.map(function(r) {
		var options = url.parse(baseUri + r);
		options.route = r;
		return proxy(options);
	});
	proxies.push(historyApiFallback());

	browserSync({
		open: true,
		notify: false,
		port: config.port,
		server: {
			baseDir: config.app,
			middleware: proxies
		}
	});
};
