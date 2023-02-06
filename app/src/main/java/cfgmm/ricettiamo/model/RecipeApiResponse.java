package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeApiResponse implements Parcelable {

    @SerializedName("results")
    private List<Recipe> listRecipes;
    @SerializedName("ingredients")
    private List<Ingredient> ingredientsList;
    private int totalResults;

    public RecipeApiResponse() {}
    public RecipeApiResponse(List<Recipe> listRecipes, List<Ingredient> ingredientsList, int totalResults) {
        this.listRecipes = listRecipes;
        this.ingredientsList = ingredientsList;
        this.totalResults = totalResults;
    }

    protected RecipeApiResponse(Parcel in) {
        this.listRecipes = in.createTypedArrayList(Recipe.CREATOR);
        this.ingredientsList = in.createTypedArrayList(Ingredient.CREATOR);
        this.totalResults = in.readInt();
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Ingredient> getListIngredients() { return ingredientsList; }

    public void setListIngredients(List<Ingredient> ingredientList) { this.ingredientsList = ingredientList; }

    public List<Recipe> getListRecipes() { return listRecipes; }

    public void setListRecipes(List<Recipe> listRecipes) {
        this.listRecipes = listRecipes;
    }

    @Override
    public String toString() {
        return "RecipeApiResponse{" +
                ", listRecipes=" + listRecipes +
                ", ingredientsList=" + ingredientsList +
                ", totalResults=" + totalResults +
                '}';
    }

    public static final Parcelable.Creator<RecipeApiResponse> CREATOR = new Creator<RecipeApiResponse>() {
        @Override
        public RecipeApiResponse createFromParcel(Parcel in) {
            return new RecipeApiResponse(in);
        }

        @Override
        public RecipeApiResponse[] newArray(int size) {
            return new RecipeApiResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.listRecipes);
        dest.writeTypedList(this.ingredientsList);
        dest.writeInt(this.totalResults);
    }
    public void readFromParcel(Parcel source) {
        this.listRecipes = source.createTypedArrayList(Recipe.CREATOR);
        this.ingredientsList = source.createTypedArrayList(Ingredient.CREATOR);
        this.totalResults = source.readInt();
    }
}
