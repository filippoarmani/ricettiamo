package cfgmm.ricettiamo.model;

import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import java.util.Date;

import cfgmm.ricettiamo.R;

public class User {

    private int id;

    private Uri photo;

    private String name;
    private String surname;
    private String displayName;

    private String email;

    private String description;

    private Date dateBirth;
    private PhoneNumberUtils number;

    public User(String name, String surname, String email, Date dateBirth, PhoneNumberUtils number) {
        this.photo = Uri.parse(String.valueOf(R.id.user));
        this.name = name;
        this.surname = surname;
        this.displayName = name + " " + surname;
        this.email = email;
        this.dateBirth = dateBirth;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public PhoneNumberUtils getNumber() {
        return number;
    }

    public void setNumber(PhoneNumberUtils number) {
        this.number = number;
    }
}
