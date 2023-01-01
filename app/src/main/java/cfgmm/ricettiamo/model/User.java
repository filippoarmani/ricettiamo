package cfgmm.ricettiamo.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    private String id;

    private String photo;

    private String name;
    private String surname;

    private String displayName;
    private String description;

    private int totalStars;
    private int positions;

    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String photo, String name, String surname, String email) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.displayName = name + " " + surname;
        this.description = "";
        this.totalStars = 0;
        this.positions = -1;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser='" + id + '\'' +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", id);
        data.put("photo", photo);
        data.put("name", name);
        data.put("surname", surname);
        data.put("displayName", displayName);
        data.put("description", description);

        data.put("email", email);

        return data;
    }

    public String getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    public int getPositions() {
        return positions;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public void setPositions(int positions) {
        this.positions = positions;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
