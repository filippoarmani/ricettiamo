package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Ingredient implements Parcelable {
    //used for room
    @PrimaryKey (autoGenerate = true)
    private long id;
    private String name;
    @SerializedName("amount")
    private float qta;
    @SerializedName("unit")
    private String size;

    public Ingredient() {}

    @Ignore
    public Ingredient(String name, float qta, String size) {
        this(0, name, qta, size);
    }

    @Ignore
    public Ingredient(long id, String name, float qta, String size) {
        this.id = id;
        this.name = name;
        this.qta = qta;
        this.size = size;
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

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public float getQta() { return qta;  }

    public void setQta(float qta) { this.qta = qta; }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", nome='" + name + '\'' +
                ", qta='" + qta + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    protected Ingredient(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.qta = in.readFloat();
        this.size = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.qta);
        dest.writeString(this.size);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", id);
        data.put("name", name);
        data.put("qta", qta);
        data.put("size", size);

        return data;
    }
}
