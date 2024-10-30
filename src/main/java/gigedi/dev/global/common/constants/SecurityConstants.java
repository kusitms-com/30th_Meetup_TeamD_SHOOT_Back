package gigedi.dev.global.common.constants;

public final class SecurityConstants {
    public static final String CODE_KEY = "code";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID_SECRET = "client_secret";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String GRANT_TYPE_KEY = "grant_type";

    public static final String GET_ID_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_ISSUER = "https://accounts.google.com";
    public static final String GOOGLE_JWK_SET_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public static final String TOKEN_ROLE_NAME = "role";

    private SecurityConstants() {
        throw new AssertionError();
    }
}
