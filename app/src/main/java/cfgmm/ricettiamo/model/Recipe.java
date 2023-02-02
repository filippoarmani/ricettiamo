package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import cfgmm.ricettiamo.R;

@Entity
public class Recipe implements Parcelable {
    //used for room
    @PrimaryKey
    private long id;
    @SerializedName("sourceName")
    private String author;
    @SerializedName("title")
    private String name;
    private int score;
    private int servings;
    @SerializedName("pricePerServing")
    private float cost;
    @SerializedName("readyInMinutes")
    private int prepTime;
    private String ingredients;
    private String date;
    //private String url;
    @SerializedName("image")
    private String urlToImage;
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    public Recipe() {}
    public Recipe(String author, String name, int score, int servings, float cost, int prepTime,
                  String ingredients, String date, /*String url, */String urlToImage, boolean isFavorite) {
        this.author = author;
        this.name = name;
        this.score = score;
        this.servings = servings;
        this.cost = cost;
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.date = date;
        //this.url = url;
        this.urlToImage = urlToImage;
        this.isFavorite = isFavorite;
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getServings() { return servings; }

    public void setServings(int servings) { this.servings = servings; }

    public float getCost() { return cost; }

    public void setCost(float cost) { this.cost = cost / 100; }

    public int getPrepTime() { return prepTime; }

    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }

    public String getIngredients() { return ingredients; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }*/

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=' " + id + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score + '\'' +
                ", servings=" + servings + '\'' +
                ", cost=" + cost/100 + "€" + '\'' +
                ", prepTime=" + prepTime + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", date='" + date + '\'' +
                //", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", isFavorite=" + isFavorite + '\'' +
                '}';
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        author = in.readString();
        name = in.readString();
        score = in.readInt();
        servings = in.readInt();
        cost = in.readFloat();
        prepTime = in.readInt();
        ingredients = in.readString();
        date = in.readString();
        //url = in.readString();
        urlToImage = in.readString();
        isFavorite = in.readByte() != 0;

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.author);
        dest.writeString(this.name);
        dest.writeInt(this.score);
        dest.writeInt(this.servings);
        dest.writeFloat(this.cost);
        dest.writeInt(this.prepTime);
        dest.writeString(this.ingredients);
        dest.writeString(this.date);
        //dest.writeString(this.url);
        dest.writeString(this.urlToImage);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", this.id);
        data.put("author", this.author);
        data.put("name", this.name);
        data.put("score", this.score);
        data.put("servings", this.servings);
        data.put("cost", this.cost);
        data.put("prepTime", this.prepTime);
        data.put("ingredients", this.ingredients);
        data.put("date", this.date);
        //data.put("url", this.url);
        data.put("urlToImage", this.urlToImage);
        data.put("isFavorite", this.isFavorite);

        return data;
    }
}
