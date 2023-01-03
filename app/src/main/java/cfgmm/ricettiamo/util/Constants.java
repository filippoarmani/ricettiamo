package cfgmm.ricettiamo.util;

public class Constants {
    //UserDataRemoteDataSource
    public static final String FIREBASE_REALTIME_DATABASE = "https://ricettiamo-8cacf-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";

    //UserAuthenticationRemoteDataSource
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    //for room database
    public static final String RECIPES_DATABASE_NAME = "recipes_db";
    public static final int DATABASE_VERSION = 1;

    //for spoonacular api (https://spoonacular.com/)
    public static final String RECIPES_API_BASE_URL = "https://api.spoonacular.com/";
}
