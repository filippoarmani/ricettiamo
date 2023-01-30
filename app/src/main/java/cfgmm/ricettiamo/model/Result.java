package cfgmm.ricettiamo.model;

import android.net.Uri;

import java.util.List;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        if (this instanceof RecipeResponseSuccess ||
                this instanceof UserResponseSuccess  ||
                this instanceof PhotoResponseSuccess ||
                this instanceof TopTenResponseSuccess||
                this instanceof PositionResponseSuccess ||
                this instanceof CommentResponseSuccess) {
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

    public static final class TopTenResponseSuccess extends Result {
        private final User[] topTen;
        public TopTenResponseSuccess(User[] topTen) {
            this.topTen = topTen;
        }
        public User[] getData() {
            return topTen;
        }
    }

    public static final class PositionResponseSuccess extends Result {
        private final int position;
        public PositionResponseSuccess(int position) {
            this.position = position;
        }
        public int getData() {
            return position;
        }
    }

    public static final class CommentResponseSuccess extends Result {
        private final List<Comment> commentList;
        public CommentResponseSuccess(List<Comment> commentList) {
            this.commentList = commentList;
        }
        public List<Comment> getData() {
            return commentList;
        }
    }

    public static final class Error extends Result {
        private final int message;
        public Error(int message) {
            this.message = message;
        }
        public int getMessage() {
            return message;
        }
    }
}
