package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @SerializedName("missedIngredients")
    private List<Ingredient> ingredientsList;
    private String date;
    private List<String> dishTypes;
    @SerializedName("image")
    private String urlToImage;
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;
    @SerializedName("analyzedInstructions")
    private List<StepsAnalyze> steps;

    public Recipe() {}

    @Ignore
    public Recipe(String author, String name, int score, int servings, float cost, int prepTime,
                  List<Ingredient> ingredientsList, String date, List<String> dishTypes,
                  String urlToImage, boolean isFavorite, List<StepsAnalyze> steps) {
        this(0, author, name, score, servings, cost, prepTime, ingredientsList, date, dishTypes, urlToImage, isFavorite, steps);
    }

    @Ignore
    public Recipe(long id, String author, String name, int score, int servings, float cost, int prepTime,
                  List<Ingredient> ingredientsList, String date, List<String> dishTypes,
                  String urlToImage, boolean isFavorite, List<StepsAnalyze> steps) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.score = score;
        this.servings = servings;
        this.cost = cost;
        this.prepTime = prepTime;
        this.ingredientsList = ingredientsList;
        this.date = date;
        this.dishTypes = dishTypes;
        this.urlToImage = urlToImage;
        this.isFavorite = isFavorite;
        this.steps = steps;
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

    public float getCost() { return Math.round(cost * servings) / 100.0f; }

    public void setCost(float cost) { this.cost = cost; }

    public int getPrepTime() { return prepTime; }

    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }

    public List<Ingredient> getIngredientsList() { return ingredientsList; }

    public void setIngredientsList(List<Ingredient> ingredientsList) { this.ingredientsList = ingredientsList; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public List<String> getDishTypes() { return dishTypes; }

    public void setDishTypes(List<String> dishTypes) { this.dishTypes = dishTypes; }

    public String getUrlToImage() { return urlToImage; }

    public void setUrlToImage(String urlToImage) {  this.urlToImage = urlToImage; }

    public boolean isFavorite() { return isFavorite; }

    public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    public List<StepsAnalyze> getSteps() { return steps; }

    public void setSteps(List<StepsAnalyze> steps) { this.steps = steps;  }

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
                ", ingredientsList='" + ingredientsList + '\'' +
                ", date='" + date + '\'' +
                ", dishTypes='" + dishTypes + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", isFavorite=" + isFavorite + '\'' +
                ", steps=" + steps + '\'' +
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
        if(in.readByte() == 0x01) {
            ingredientsList = new ArrayList<>();
            in.readList(ingredientsList, Ingredient.class.getClassLoader());
        } else ingredientsList = null;
        date = in.readString();
        if(in.readByte() == 0x01) {
            dishTypes = new ArrayList<>();
            in.readList(dishTypes, String.class.getClassLoader());
        } else dishTypes = null;
        urlToImage = in.readString();
        isFavorite = in.readByte() != 0;
        if(in.readByte() == 0x01) {
            steps = new ArrayList<>();
            in.readList(steps, StepsAnalyze.class.getClassLoader());
        } else steps = null;
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
        if (ingredientsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(ingredientsList));
        }
        dest.writeString(this.date);
        if (dishTypes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(dishTypes));
        }
        dest.writeString(this.urlToImage);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(score));
        }
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
        data.put("ingredientsList", this.ingredientsList);
        data.put("date", this.date);
        data.put("dishTypes", this.dishTypes);
        data.put("urlToImage", this.urlToImage);
        data.put("isFavorite", this.isFavorite);
        data.put("steps", this.steps);

        return data;
    }
}
