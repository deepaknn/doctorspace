package app.doctorspace.doctorspace.entity;

public class AuthenticationResponse {

    private final String token;

    public String getRefreshToken() {
        return refreshToken;
    }

    private final String refreshToken;

    public String getToken() {
        return token;
    }

    public AuthenticationResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
