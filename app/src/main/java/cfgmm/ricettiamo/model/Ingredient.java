package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Ingredient implements Parcelable {
    private String name;
    @SerializedName("publishedAt")
    private String qta;
    private String url;
    private String urlToImage;

public Ingredient(){}

    public Ingredient(String nome, String qta, String url, String urlToImage) {
        this.name = nome;
        this.qta = qta;
        this.url = url;
        this.urlToImage = urlToImage;
    }
    public Ingredient(String nome, String qta) {
        this(nome, qta, null, null);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getNome() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public String getQta() {
        return qta;
    }

    public void setQta(String qta) {
        this.qta = qta;
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
    public String   toString() {
        return "Ingredient{" +
                "nome='" + name + '\'' +
                ", qta='" + qta + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    public Ingredient(Parcel in) {
        this.name = in.readString();
        this.qta = in.readString();
        this.url = in.readString();
        this.urlToImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.qta);
        dest.writeString(this.url);
        dest.writeString(this.urlToImage);

    }

}
