package com.finki.emt.bookstore.web.rest.response;

public class JWTResponse {

    private String jwtToken;

    public JWTResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return String.format("JWTResponse{jwtToken='%s'}", jwtToken);
    }
}
