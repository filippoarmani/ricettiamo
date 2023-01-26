package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;

public interface IUserRepository {

    MutableLiveData<Result> signUp(User newUser, String email, String password);
    MutableLiveData<Result> signIn(String email, String password);

    void resetPassword(String email);
    void updateEmail(String email);
    void updatePassword(String oldPassword, String newPassword);
    void updateData(Map<String, Object> newInfo);
    void updatePhoto(Uri uri);

    void writeUser(User newUser);

    void readUser(String id);

    MutableLiveData<Result> getLoggedUser();
    MutableLiveData<Result> signOut();

    boolean isLoggedUser();

    MutableLiveData<Result> getCurrentPhoto();

    MutableLiveData<Result> getTopTen();

    MutableLiveData<Result> getPosition();

    MutableLiveData<Result> signInGoogle(String idToken);
}
