package cfgmm.ricettiamo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Comment implements Parcelable {

    private String idComment;
    private String idUser;
    private String idRecipe;

    private String description;
    private int score;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String idComment, String idUser, String idRecipe, String description, int score) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.idRecipe = idRecipe;
        this.description = description;
        this.score = score;
    }

    protected Comment(Parcel in) {
        idComment = in.readString();
        idUser = in.readString();
        idRecipe = in.readString();
        description = in.readString();
        score = in.readInt();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getIdComment() { return idComment; }

    public String getIdUser() {
        return idUser;
    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }

    public void setIdComment(String idComment) { this.idComment = idComment; }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdRecipe(String idRecipe) {
        this.idRecipe = idRecipe;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "idComment='" + idComment +'\'' +
                ", idUser='" + idUser + '\'' +
                ", idRecipe='" + idRecipe + '\'' +
                ", description='" + description + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("idComment", idComment);
        data.put("idUser", idUser);
        data.put("idRecipe", idRecipe);
        data.put("description", description);
        data.put("score", score);

        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.idUser);
        dest.writeString(this.idRecipe);
        dest.writeString(this.description);
        dest.writeInt(this.score);
    }
}
