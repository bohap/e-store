describe('AuthJwtProvider test', function() {
	// var AuthJWTProvider;

	beforeEach(inject(function() {

	}));

	it("should be simple test", function() {
		expect(true).toBeTruthy();
	});
});

(function() {
	'use strict';

	angular
		.module('app')
		.factory('Logger', Logger);

	Logger.$inject = [];
	function Logger() {
		var service = {
			test: test
		};

		return service;
	}
})();

