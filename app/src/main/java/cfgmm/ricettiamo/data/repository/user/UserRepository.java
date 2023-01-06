package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.source.user.BaseDatabaseDataSource;
import cfgmm.ricettiamo.data.source.user.BaseFirebaseAuthDataSource;
import cfgmm.ricettiamo.model.User;

public class UserRepository implements IUserRepository, IUserResponseCallback {

    BaseFirebaseAuthDataSource firebaseAuthDataSource;
    BaseDatabaseDataSource databaseDataSource;

    MutableLiveData<User> currentUser;
    MutableLiveData<Uri> currentPhoto;

    public UserRepository(BaseFirebaseAuthDataSource firebaseAuthDataSource,
                          BaseDatabaseDataSource databaseDataSource) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.databaseDataSource = databaseDataSource;
        this.currentUser = new MutableLiveData<>();
        this.currentPhoto = new MutableLiveData<>();

        this.firebaseAuthDataSource.setCallBack(this);
        this.databaseDataSource.setCallBack(this);
    }

    public void signUp(User newUser, String email, String password) {
        firebaseAuthDataSource.signUp(newUser, email, password);
    }

    public void signIn(String email, String password) {
        firebaseAuthDataSource.signIn(email, password);
    }

    public void resetPassword(String email) {
        firebaseAuthDataSource.resetPassword(email);
    }

    public void updateEmail(String email) {
        firebaseAuthDataSource.updateEmail(email);
    }

    public void updatePassword(String oldPassword, String newPassword) {
        firebaseAuthDataSource.updatePassword(oldPassword, newPassword);
    }

    public boolean isLoggedUser() {
        return firebaseAuthDataSource.isLoggedUser();
    }

    @Override
    public void updateData(Map<String, Object> newInfo) {
        if(newInfo.containsKey("email")) {
            firebaseAuthDataSource.updateEmail((String) newInfo.get("email"));
        }

        databaseDataSource.updateData(newInfo, firebaseAuthDataSource.getCurrentId());
    }

    @Override
    public void updatePhoto(Uri uri) {
        firebaseAuthDataSource.updatePhoto(uri);
    }

    public void writeUser(User newUser) {
        databaseDataSource.writeUser(newUser);
    }

    public void readUser(String id) {
        databaseDataSource.readUser(id);
    }

    public MutableLiveData<User> getLoggedUser() {
        String id = firebaseAuthDataSource.getCurrentId();
        readUser(id);
        return currentUser;
    }

    public MutableLiveData<Uri> getCurrentPhoto() {
        Uri uri = firebaseAuthDataSource.getCurrentPhoto();
        if(uri == null) {
            uri = Uri.parse(String.valueOf(R.drawable.user));
        }
        currentPhoto.postValue(uri);
        return currentPhoto;
    }

    public MutableLiveData<User> signOut() {
        firebaseAuthDataSource.signOut();
        return currentUser;
    }

    @Override
    public void onSuccessRegistration(User newUser) {
        writeUser(newUser);
    }

    @Override
    public void onFailureRegistration(String localizedMessage) { }

    @Override
    public void onSuccessLogin(String uid) {
        readUser(uid);
    }

    @Override
    public void onFailureLogin(String localizedMessage) { }

    @Override
    public void onSuccessResetPassword() { }

    @Override
    public void onFailureResetPassword(String localizedMessage) { }

    @Override
    public void onSuccessUpdateEmail() { }

    @Override
    public void onFailureUpdateEmail(String localizedMessage) { }

    @Override
    public void onSuccessUpdatePassword() { }

    @Override
    public void onFailureUpdatePassword(String localizedMessage) { }

    @Override
    public void onSuccessWriteDatabase() { }

    @Override
    public void onFailureWriteDatabase(String localizedMessage) { }

    @Override
    public void onSuccessReadDatabase(User user) {
        if(user != null) {
            currentUser.postValue(user);
        }
    }

    @Override
    public void onFailureReadDatabase(String localizedMessage) { }

    @Override
    public void onSuccessLogout() {
        currentUser.postValue(null);
        currentPhoto.postValue(Uri.parse(String.valueOf(R.drawable.user)));
    }

    @Override
    public void onSuccessSetPhoto(Uri uri) {
        currentPhoto.postValue(uri);
    }

    @Override
    public void onFailureSetPhoto(String localizedMessage) {}
}
