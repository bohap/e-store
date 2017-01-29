(function() {
	'use strict';

	var book = {
		template: "<ng-outlet></ng-outlet>",
		$routeConfig: [
			{
				path: '/',
				name: 'BooksIndex',
				component: 'booksIndex',
				useAsDefault: true
			},
			{
				path: '/create',
				name: 'BookCreate',
				component: 'bookCreate'
			},
			{
				path: '/:slug',
				name: 'BookDetails',
				component: 'bookDetails'
			},
			{
				path: '/:slug/update',
				name: 'BookUpdate',
				component: 'bookUpdate'
			}
		]
	};

	angular
		.module('app')
		.component('book', book);
})();
