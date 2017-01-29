(function() {
	'use strict';

	var messages = {
		notFound: {
			title: "Not Found",
			body: "The page can't be find! Please try another."
		},
		notAuthenticated: {
			title: "Not Authenticated",
			body: "You need to be logged in to see this page"
		},
		notAuthorized: {
			title: "Not Authorized",
			body: "You don't have permissions to see this page"
		},
		internalServerError: {
			title: "Serve error",
			body: "There is some problem with the servers. Please try again later"
		}
	};

	angular
		.module('app')
		.constant('MESSAGES', messages);
})();
