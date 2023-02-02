package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;



public class    IngredientApiResponse implements Parcelable {

    private String status;
    private int totalResults;
    private List<Ingredient> ingredients;

    public IngredientApiResponse() {}
    public IngredientApiResponse(String status, int totalResults, List<Ingredient> ingredients) {
        this.status = status;
        this.totalResults = totalResults;
        this.ingredients = ingredients;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> articles) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "IngredientApiResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", ingredients=" + ingredients +
                '}';
    }

    protected IngredientApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<IngredientApiResponse> CREATOR = new Creator<IngredientApiResponse>() {
        @Override
        public IngredientApiResponse createFromParcel(Parcel in) {
            return new IngredientApiResponse(in);
        }

        @Override
        public IngredientApiResponse[] newArray(int size) {
            return new IngredientApiResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.totalResults);
        dest.writeTypedList(this.ingredients);
    }
    public void readFromParcel(Parcel source) {
        this.status = source.readString();
        this.totalResults = source.readInt();
        this.ingredients = source.createTypedArrayList(Ingredient.CREATOR);
    }

    public String ListIngredientsToString(List<Ingredient> ingredientList) {
        String ingredientsString = "";

        for (int i = 0; i<ingredientList.size(); i++) {
            ingredientsString += ingredientList.get(i).getName() + ",";
        }

        return ingredientsString;
    }

}