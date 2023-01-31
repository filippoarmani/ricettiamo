package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeApiResponse implements Parcelable {

    private String status;
    private int totalResults;
    private List<Recipe> listRecipes;

    public RecipeApiResponse() {}
    public RecipeApiResponse(String status, int totalResults, List<Recipe> listRecipes) {
        this.status = status;
        this.totalResults = totalResults;
        this.listRecipes = listRecipes;
    }

    protected RecipeApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.listRecipes = in.createTypedArrayList(Recipe.CREATOR);
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

    public List<Recipe> getListRecipes() {
        return listRecipes;
    }

    public void setListRecipes(List<Recipe> listRecipes) {
        this.listRecipes = listRecipes;
    }

    @Override
    public String toString() {
        return "RecipeApiResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", listRecipes=" + listRecipes +
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
        dest.writeString(this.status);
        dest.writeInt(this.totalResults);
        dest.writeTypedList(this.listRecipes);
    }
    public void readFromParcel(Parcel source) {
        this.status = source.readString();
        this.totalResults = source.readInt();
        this.listRecipes = source.createTypedArrayList(Recipe.CREATOR);
    }
}
