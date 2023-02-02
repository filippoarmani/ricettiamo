package cfgmm.ricettiamo.util;

public class Constants {
    //UserDataRemoteDataSource
    public static final String FIREBASE_REALTIME_DATABASE = "https://ricettiamo-8cacf-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_RECIPES_COLLECTION = "recipes";
    public static final String FIREBASE_COMMENTS_COLLECTION = "comments";
    public static final String FIREBASE_FAVORITE_RECIPES_COLLECTION = "favorute_recipes";

    //UserAuthenticationRemoteDataSource
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    //for room database
    public static final String RECIPES_DATABASE_NAME = "recipes_db";
    public static final String RECIPES_API_TEST_JSON_FILE = "recipes-test.json";
    public static final String INGREDIENTS_API_TEST_JSON_FILE = "ingredients-id.json";
    public static final int DATABASE_VERSION = 1;

    //for spoonacular api (https://spoonacular.com/)
    public static final int NUMBER_OF_ELEMENTS = 20;
    public static final String RECIPES_API_BASE_URL = "https://api.spoonacular.com/";
    public static final String SEARCH_RECIPES = "recipes/complexSearch";
    public static final String SEARCH_RECIPES_BY_INGREDIENT = "recipes/findByIngredients";
    public static final String SEARCH_INGREDIENT = "food/ingredients/search";
    public static final String RECIPE_PARAMETER = "query";
    public static final String INGREDIENTS_LIST = "ingredients";

    //for errors
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String RETROFIT_ERROR = "retrofit_error";

    public static final String[] IMAGE = {"image/*"};
}
