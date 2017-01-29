(function() {
	'use strict';

	angular
		.module('app')
		.controller('LoginController', LoginController);

	LoginController.$inject = ['Auth', '$uibModalInstance', 'ArrayUtils', '$rootRouter'];

	function LoginController(Auth, $uibModalInstance, ArrayUtils, $rootRouter) {
		var vm = this;

		vm.cancel = cancel;
		vm.login = login;
		vm.errors = [];
		vm.isSending = false;
		vm.username = null;
		vm.password = null;

		function cancel() {
			$uibModalInstance.dismiss('cancel');
		}

		function login() {
			if (vm.isSending) {
				return;
			}

			var credentials = {
				username: vm.username,
				password: vm.password
			};

			vm.isSending = true;
			vm.errors = [];
			Auth.login(credentials)
				.then(loginSuccess, loginFailed);

			function loginSuccess(data) {
				var route = $rootRouter.lastNavigationAttempt;
				$rootRouter.navigateByUrl(route);
				vm.isSending = false;
				$uibModalInstance.close('success');
			}

			function loginFailed(data) {
				vm.isSending = false;
				if (angular.isObject(data) && angular.isDefined(data.errors)) {
					vm.errors = ArrayUtils.flatten(data.errors);
				}
			}
		}
	}
})();
