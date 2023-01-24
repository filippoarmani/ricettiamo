package cfgmm.ricettiamo.model;

import android.net.Uri;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        if (this instanceof RecipeResponseSuccess ||
                this instanceof UserResponseSuccess ||
                this instanceof PhotoResponseSuccess) {
            return true;
        } else {
            return false;
        }
    }

    public static final class RecipeResponseSuccess extends Result {
        private final RecipeResponse recipeResponse;
        public RecipeResponseSuccess(RecipeResponse recipeResponse) {
            this.recipeResponse = recipeResponse;
        }
        public RecipeResponse getData() {
            return recipeResponse;
        }
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class PhotoResponseSuccess extends Result {
        private final Uri uri;
        public PhotoResponseSuccess(Uri uri) {
            this.uri = uri;
        }
        public Uri getData() {
            return uri;
        }
    }

    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
