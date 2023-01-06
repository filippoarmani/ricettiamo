package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import cfgmm.ricettiamo.model.User;

public interface IUserRepository {

    void signUp(User newUser, String email, String password);
    void signIn(String email, String password);

    void resetPassword(String email);
    void updateEmail(String email);
    void updatePassword(String oldPassword, String newPassword);
    void updateData(Map<String, Object> newInfo);
    void updatePhoto(Uri uri);

    MutableLiveData<User> getLoggedUser();
    MutableLiveData<User> signOut();

    boolean isLoggedUser();

    MutableLiveData<Uri> getCurrentPhoto();
}
