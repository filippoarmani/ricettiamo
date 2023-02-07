package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

public class Step implements Parcelable {

    private int number;
    @SerializedName("step")
    private String description;

    public Step() {}
    public Step (int number, String description) {
        this.number = number;
        this.description = description;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Step{" +
                "number='" + number + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    protected Step(Parcel in) {
        this.number = in.readInt();
        this.description = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeString(this.description);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("number", number);
        data.put("description", description);

        return data;
    }
}
