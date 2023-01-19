package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

/*import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;*/

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe implements Parcelable {
    //used for room
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String author;
    private String name;
    private int score;
    private String description;

    private String[] ingredients;
    private String date;
    private String url;
    private String urlToImage;
    private boolean isFavorite;

    public Recipe(String author, String name, int score, String description, String date,
                  String url, String urlToImage, boolean isFavorite) {
        this.author = author;
        this.name = name;
        this.score = score;
        this.description = description;
        this.date = date;
        this.url = url;
        this.urlToImage = urlToImage;
        this.isFavorite = isFavorite;
    }

    /*public Recipe(String author, String name, int score, String description, String date) {
        this(author, name, score, description, date,null, null, false);
    }*/

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

    public String getDescription() { return description; }

    public String[] getIngredients() { return ingredients; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public boolean getIsFavorite() { return isFavorite; }

    public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=' " + id + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", isFavorite=" + isFavorite + '\'' +
                '}';
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        author = in.readString();
        name = in.readString();
        score = in.readInt();
        description = in.readString();
        //ingredients = in.readArray(ingredients);
        date = in.readString();
        url = in.readString();
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
        dest.writeString(this.description);
        dest.writeArray(this.ingredients);
        dest.writeString(this.date);
        dest.writeString(this.url);
        dest.writeString(this.urlToImage);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }
}
