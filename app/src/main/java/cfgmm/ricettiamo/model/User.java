package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Parcelable {

    private String id;
    private String signInMethod;

    private String fullName;

    private String displayName;
    private String description;

    private int totalStars;

    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String signInMethod, String fullName, String email) {
        this.id = id;
        this.signInMethod = signInMethod;
        this.fullName = fullName;
        this.displayName = fullName;
        this.description = "";
        this.totalStars = 0;
        this.email = email;
    }

    protected User(Parcel in) {
        id = in.readString();
        signInMethod = in.readString();
        fullName = in.readString();
        displayName = in.readString();
        description = in.readString();
        totalStars = in.readInt();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "idUser='" + id + '\'' +
                ", signInMethod='" + signInMethod + '\'' +
                ", fullName='" + fullName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", totalStars='" + totalStars + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", id);
        data.put("signInMethod", signInMethod);
        data.put("fullName", fullName);
        data.put("displayName", displayName);
        data.put("description", description);

        data.put("email", email);
        data.put("totalStars", totalStars);

        return data;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignInMethod() {
        return signInMethod;
    }

    public void setSignInMethod(String signInMethod) {
        this.signInMethod = signInMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof User))
            return false;

        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (signInMethod == null) {
            if (other.signInMethod != null)
                return false;
        } else if (!signInMethod.equals(other.signInMethod))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.signInMethod);
        dest.writeString(this.fullName);
        dest.writeString(this.displayName);
        dest.writeString(this.description);
        dest.writeInt(this.totalStars);
        dest.writeString(this.email);
    }
}
