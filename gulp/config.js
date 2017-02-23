'use strict';
module.exports = {
	app: 'src/main/webapp/',
	dist: 'src/main/webapp/dist/',
	bower: 'src/main/webapp/assets/libs/',
	assets: 'src/main/webapp/assets/',
	eslintReport: 'build/reports/eslint',
	module: 'app',
	uri: 'http://localhost:',
	port: 9000,
	apiUrl: 'http://localhost:8080',
	proxyRoutes: [
		'/api'
	]
};
