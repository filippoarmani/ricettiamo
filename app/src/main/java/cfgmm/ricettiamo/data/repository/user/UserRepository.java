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
    MutableLiveData<Result> topTenList;
    MutableLiveData<Result> position;

    public UserRepository(BaseFirebaseAuthDataSource firebaseAuthDataSource,
                          BaseDatabaseDataSource databaseDataSource) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.databaseDataSource = databaseDataSource;
        this.currentUser = new MutableLiveData<>();
        this.currentPhoto = new MutableLiveData<>();
        this.topTenList = new MutableLiveData<>();
        this.position = new MutableLiveData<>();

        this.firebaseAuthDataSource.setCallBack(this);
        this.databaseDataSource.setCallBack(this);
    }

    @Override
    public MutableLiveData<Result> signUp(User newUser, String email, String password) {
        firebaseAuthDataSource.signUp(newUser, email, password);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> signIn(String email, String password) {
        firebaseAuthDataSource.signIn(email, password);
        return currentUser;
    }

    @Override
    public void resetPassword(String email) {
        firebaseAuthDataSource.resetPassword(email);
    }

    @Override
    public void updateEmail(String email) {
        firebaseAuthDataSource.updateEmail(email);
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        firebaseAuthDataSource.updatePassword(oldPassword, newPassword);
    }

    @Override
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

    @Override
    public void writeUser(User newUser) {
        databaseDataSource.writeUser(newUser);
    }

    @Override
    public void readUser(String id) {
        databaseDataSource.readUser(id);
    }

    public MutableLiveData<Result> getLoggedUser() {
        String id = firebaseAuthDataSource.getCurrentId();
        this.readUser(id);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> getCurrentPhoto() {
        firebaseAuthDataSource.getCurrentPhoto();
        return currentPhoto;
    }

    @Override
    public MutableLiveData<Result> signOut() {
        firebaseAuthDataSource.signOut();
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> getTopTen() {
        databaseDataSource.getTopTen();
        return topTenList;
    }

    @Override
    public MutableLiveData<Result> getPosition() {
        databaseDataSource.getPosition(firebaseAuthDataSource.getCurrentId());
        return position;
    }

    @Override
    public MutableLiveData<Result> signInGoogle(String idToken) {
        firebaseAuthDataSource.signInGoogle(idToken);
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
    public void onFailureUpdateEmail(int idError) { currentUser.postValue(new Result.Error(idError)); }

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
        firebaseAuthDataSource.delete();
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
    public void onSuccessSetPhoto() {
        getCurrentPhoto();
    }

    @Override
    public void onFailureSetPhoto(int idError) {
        currentPhoto.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessGetTopTen(User[] topTen) {
        topTenList.postValue(new Result.TopTenResponseSuccess(topTen));
    }

    @Override
    public void onFailureGetTopTen(int idError) {
        topTenList.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessGetPosition(int i) {
        position.postValue(new Result.PositionResponseSuccess(i));
    }

    @Override
    public void onFailureGetPosition(int idError) {
        position.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessDelete() { }

    @Override
    public void onFailureDelete() {
        firebaseAuthDataSource.delete();
    }

    @Override
    public void onSuccessUpdateDatabase() { }

    @Override
    public void onFailureUpdateDatabase(int idError) { }

    @Override
    public void onSuccessLoginGoogle(User user) {
        if(databaseDataSource.alreadyExist(user)) {
            this.readUser(user.getId());
        } else {
            this.writeUser(user);
        }
    }
}
