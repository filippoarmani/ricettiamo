package cfgmm.ricettiamo.model;

import android.media.audiofx.DynamicsProcessing;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Step implements Parcelable {

    private int number;
    @SerializedName("step")
    private String description;
    private List<Ingredient> ingredients;
    private List<Equipment> equipment;

    public Step() {}
    public Step (int number, String description, List<Ingredient> ingredients, List<Equipment> equipment) {
        this.number = number;
        this.description = description;
        this.ingredients = ingredients;
        this.equipment = equipment;
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

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Equipment> getEquipment() { return equipment; }
    public void setEquipment(List<Equipment> equipment) { this.equipment = equipment; }

    @Override
    public String toString() {
        return "Step{" +
                "number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", equipments='" + equipment + '\'' +
                '}';
    }

    protected Step(Parcel in) {
        this.number = in.readInt();
        this.description = in.readString();
        if(in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else ingredients = null;
        if(in.readByte() == 0x01) {
            equipment = new ArrayList<Equipment>();
            in.readList(equipment, Equipment.class.getClassLoader());
        } else equipment = null;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeString(this.description);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(ingredients));
        }
        if (equipment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Collections.singletonList(equipment));
        }
    }
}
