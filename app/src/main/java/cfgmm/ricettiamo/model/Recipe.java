package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

/*import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;*/

import androidx.annotation.NonNull;

public class Recipe implements Parcelable {
    //used for room
    //@PrimaryKey(autoGenerate = true)
    //private long id;

    private String author;
    private String name;
    private int score;
    private String description;
    private String date;
    private String url;
    private String urlToImage;

    public Recipe(String author, String name, int score, String description, String date,
                  String url, String urlToImage) {
        this.author = author;
        this.name = name;
        this.score = score;
        this.description = description;
        this.date = date;
        this.url = url;
        this.urlToImage = urlToImage;
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

    public String getDescription() {
        return description;
    }

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

    @Override
    public String toString() {
        return "Recipe{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    protected Recipe(Parcel in) {
        author = in.readString();
        name = in.readString();
        score = in.readInt();
        description = in.readString();
        date = in.readString();
        url = in.readString();
        urlToImage = in.readString();

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
        dest.writeString(author);
        dest.writeString(name);
        dest.writeInt(score);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(url);
        dest.writeString(urlToImage);
    }
}
