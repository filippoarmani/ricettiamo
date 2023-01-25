package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.source.user.BaseDatabaseDataSource;
import cfgmm.ricettiamo.data.source.user.BaseFirebaseAuthDataSource;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;

public class UserRepository implements IUserRepository, IUserResponseCallback {

    BaseFirebaseAuthDataSource firebaseAuthDataSource;
    BaseDatabaseDataSource databaseDataSource;

    MutableLiveData<Result> currentUser;
    MutableLiveData<Result> currentPhoto;

    public UserRepository(BaseFirebaseAuthDataSource firebaseAuthDataSource,
                          BaseDatabaseDataSource databaseDataSource) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.databaseDataSource = databaseDataSource;
        this.currentUser = new MutableLiveData<>();
        this.currentPhoto = new MutableLiveData<>();

        this.firebaseAuthDataSource.setCallBack(this);
        this.databaseDataSource.setCallBack(this);
    }

    public MutableLiveData<Result> signUp(User newUser, String email, String password) {
        firebaseAuthDataSource.signUp(newUser, email, password);
        return currentUser;
    }

    public MutableLiveData<Result> signIn(String email, String password) {
        firebaseAuthDataSource.signIn(email, password);
        return currentUser;
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

    public MutableLiveData<Result> getLoggedUser() {
        String id = firebaseAuthDataSource.getCurrentId();
        readUser(id);
        return currentUser;
    }

    public MutableLiveData<Result> getCurrentPhoto() {
        firebaseAuthDataSource.getCurrentPhoto();
        return currentPhoto;
    }

    public MutableLiveData<Result> signOut() {
        firebaseAuthDataSource.signOut();
        return currentUser;
    }

    @Override
    public void onSuccessRegistration(User newUser) {
        writeUser(newUser);
    }

    @Override
    public void onFailureRegistration(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessLogin(String uid) {
        readUser(uid);
    }

    @Override
    public void onFailureLogin(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessUpdateEmail() { }

    @Override
    public void onFailureUpdateEmail(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessUpdatePassword() { }

    @Override
    public void onFailureUpdatePassword(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessWriteDatabase() { }

    @Override
    public void onFailureWriteDatabase(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessReadDatabase(User user) {
        currentUser.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onFailureReadDatabase(int idError) {
        currentUser.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessLogout() {
        currentUser.postValue(new Result.UserResponseSuccess(null));
        currentPhoto.postValue(new Result.PhotoResponseSuccess(Uri.parse(String.valueOf(R.drawable.user))));
    }

    @Override
    public void onSuccessGetPhoto(Uri uri) {
        currentPhoto.postValue(new Result.PhotoResponseSuccess(uri));
    }

    @Override
    public void onFailureGetPhoto(int idError) {
        currentPhoto.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessSetPhoto(Uri uri) {
        currentPhoto.postValue(new Result.PhotoResponseSuccess(uri));
    }

    @Override
    public void onFailureSetPhoto(int idError) {
        currentPhoto.postValue(new Result.Error(idError));
    }
}
