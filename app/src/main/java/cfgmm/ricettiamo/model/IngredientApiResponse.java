package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;



public class IngredientApiResponse implements Parcelable {

    private String status;
    private int totalResults;
    private List<Ingredient> articles;

    public IngredientApiResponse(String status, int totalResults, List<Ingredient> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
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

    public List<Ingredient> getArticles() {
        return articles;
    }

    public void setArticles(List<Ingredient> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "IngredientApiResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }

    protected IngredientApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.articles = in.createTypedArrayList(Ingredient.CREATOR);
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
        dest.writeTypedList(this.articles);
    }
    public void readFromParcel(Parcel source) {
        this.status = source.readString();
        this.totalResults = source.readInt();
        this.articles = source.createTypedArrayList(Ingredient.CREATOR);
    }

}