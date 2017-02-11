(function() {
	'use strict';

	angular
		.module('app.nav')
		.controller('NavbarController', NavbarController);

	NavbarController.$inject = ['LoginDialog', 'RegisterDialog', 'Principal', 'Auth'];

	function NavbarController(LoginDialog, RegisterDialog, Principal, Auth) {
		var vm = this;

		vm.isNavCollapsed = true;
		vm.toggleNavbar = toggleNavbar;
		vm.showLoginDialog = showLoginDialog;
		vm.showRegisterDialog = showRegisterDialog;
		vm.logout = logout;
		vm.isAuthenticated = Principal.isAuthenticated;
		vm.isAdmin = Principal.isAdmin;
		vm.isRegularUser = Principal.isRegularUser;
		vm.identity = Principal.getIdentity;

		function toggleNavbar() {
			vm.isNavCollapsed = !vm.isNavCollapsed;
		}

		function showLoginDialog() {
			LoginDialog.open();
		}

		function showRegisterDialog() {
			RegisterDialog.open();
		}

		function logout() {
			Auth.logout();
		}
	}
})();
