package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Equipment implements Parcelable {
    private String name;
    public Equipment() {}
    public Equipment(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                '}';
    }
    protected Equipment(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Equipment> CREATOR = new Creator<Equipment>() {
        @Override
        public Equipment createFromParcel(Parcel in) {
            return new Equipment(in);
        }

        @Override
        public Equipment[] newArray(int size) {
            return new Equipment[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
    }
}
