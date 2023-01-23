package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse implements Parcelable {
    private boolean isLoading;

    @SerializedName("articles")
    private List<Recipe> recipesList;

    public RecipeResponse() {}

    public RecipeResponse(List<Recipe> recipesList) {
        this.recipesList = recipesList;
    }

    public List<Recipe> getRecipesList() {
        return recipesList;
    }

    public void setRecipesList(List<Recipe> recipesList) {
        this.recipesList = recipesList;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
                "recipesList=" + recipesList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.recipesList);
    }

    public void readFromParcel(Parcel source) {
        this.isLoading = source.readByte() != 0;
        this.recipesList = source.createTypedArrayList(Recipe.CREATOR);
    }

    protected RecipeResponse(Parcel in) {
        this.isLoading = in.readByte() != 0;
        this.recipesList = in.createTypedArrayList(Recipe.CREATOR);
    }

    public static final Parcelable.Creator<RecipeResponse> CREATOR = new Parcelable.Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel source) {
            return new RecipeResponse(source);
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };
}
