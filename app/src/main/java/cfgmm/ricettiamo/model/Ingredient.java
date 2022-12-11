package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {
    private String name;
    private int qta;
    private String size;
    //opzionale
    private String urlToImage;

    public Ingredient(String name, int qta, String size, String urlToImage) {
        this.name = name;
        this.qta = qta;
        this.size = size;
        this.urlToImage = urlToImage;
    }


    public Ingredient(String name, int qta, String size) {
        this(name, qta, size, null);
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "nome='" + name + '\'' +
                ", qta='" + qta + '\'' +
                ", size='" + size + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    public Ingredient(Parcel in) {
        this.name = in.readString();
        this.qta = in.readInt();
        this.size = in.readString();
        this.urlToImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.qta);
        dest.writeString(this.size);
        dest.writeString(this.urlToImage);

    }
}
