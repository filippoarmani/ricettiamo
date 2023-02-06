package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepsAnalyze implements Parcelable {

    private String name;
    private List<Step> steps;

    public StepsAnalyze() {}
    public StepsAnalyze (String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Step> getSteps() { return steps; }

    public void setSteps(List<Step> steps) { this.steps = steps; }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                ", steps='" + steps + '\'' +
                '}';
    }

    protected StepsAnalyze(Parcel in) {
        this.name = in.readString();
        if(in.readByte() == 0x01) {
            steps = new ArrayList<Step>();
            in.readList(steps, Step.class.getClassLoader());
        } else steps = null;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(steps));
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", name);
        data.put("steps", steps);

        return data;
    }

}
