(function() {
	'use strict';

	angular
		.module('app')
		.run(principalRun);

	principalRun.$inject = ['Principal'];

	function principalRun(Principal) {
		Principal.identity()
			.catch(identityResolveFailed);

		function identityResolveFailed(data) {
			Principal.invalidate();
		}
	}
})();
