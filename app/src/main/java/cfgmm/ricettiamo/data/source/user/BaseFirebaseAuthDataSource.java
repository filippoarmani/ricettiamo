package cfgmm.ricettiamo.data.source.user;

import android.net.Uri;

import cfgmm.ricettiamo.data.repository.user.IUserResponseCallback;
import cfgmm.ricettiamo.model.User;

public abstract class BaseFirebaseAuthDataSource {

    IUserResponseCallback userResponseCallBack;

    public void setCallBack(IUserResponseCallback userRepository) {
        this.userResponseCallBack = userRepository;
    }

    public abstract void signUp(User newUser, String email, String password);
    public abstract void signIn(String email, String password);

    public abstract void signOut();

    public abstract void resetPassword(String email);
    public abstract void updateEmail(String email);
    public abstract void updatePassword(String oldPassword, String newPassword);

    public abstract void getCurrentPhoto();
    public abstract void updatePhoto(Uri uri);

    public abstract boolean isLoggedUser();
    public abstract String getCurrentId();

    public abstract void delete();
}
