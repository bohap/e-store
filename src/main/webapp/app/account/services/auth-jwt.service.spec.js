'use strict';
describe('AuthJwtProvider test', function() {
	var AuthJWTProvider, $localStorage;

	beforeEach(angular.mock.module('app.auth'));

	beforeEach(inject(function(_AuthJWTProvider_, _$localStorage_) {
		AuthJWTProvider = _AuthJWTProvider_;
		$localStorage = _$localStorage_;
	}));

	describe("store token", function() {
		it("should not store the token on undefined response", function() {
			var result = AuthJWTProvider.storeToken();
			expect(result).toBeFalsy();
			expect($localStorage.jwt).toBeUndefined();
		});

		it("should not store the token on null response", function() {
			var result = AuthJWTProvider.storeToken(null);
			expect(result).toBeFalsy();
			expect($localStorage.jwt).toBeUndefined();
		});

		it("should save the token when the response data is valid", function() {
			var response = { data: { jwtToken: "JWT token" } };
			var result = AuthJWTProvider.storeToken(response);
			expect(result).toBeTruthy();
			expect($localStorage.jwt).toEqual(response.data.jwtToken);
		});

		it("should check the response headers when then response data don't contains the token", function() {
			var token = "JWT token";
			var response = {
				data: {},
				headers: function(key) {
					if (key === 'Authorization') {
						return "Bearer " + token;
					}
					return null;
				}
			};
			var result = AuthJWTProvider.storeToken(response);
			expect(result).toBeTruthy();
			expect($localStorage.jwt).toEqual(token);
		});

		it("should return false when the header token is not bearer", function() {
			var token = "JWT token";
			var response = {
				data: {},
				headers: function(key) {
					if (key === 'Authorization') {
						return token;
					}
					return null;
				}
			};
			var result = AuthJWTProvider.storeToken(response);
			expect(result).toBeFalsy();
			expect($localStorage.jwt).toBeUndefined();
		});

		it("should return false when neither response or header don't contains the token", function() {
			var response = {
				data: {},
				headers: function(key) {
					return null;
				}
			};
			var result = AuthJWTProvider.storeToken(response);
			expect(result).toBeFalsy();
			expect($localStorage.jwt).toBeUndefined();
		});
	});

	describe("get token", function() {
		it("should the return the token from the local storege", function() {
			var token = "JWT token";
			$localStorage.jwt = token;
			expect(AuthJWTProvider.getToken()).toEqual(token);
		});
	});

	describe("delete token", function() {
		it("should delete the token from the local storage", function() {
			var token = "JWT token";
			$localStorage.jwt = token;
			AuthJWTProvider.deleteToken();
			expect($localStorage.jwt).toBeUndefined();
		});
	});
});
