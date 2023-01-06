package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;

import cfgmm.ricettiamo.model.User;

public interface IUserResponseCallback {
    void onSuccessRegistration(User newUser);
    void onFailureRegistration(String localizedMessage);

    void onSuccessLogin(String uid);
    void onFailureLogin(String localizedMessage);

    void onSuccessResetPassword();
    void onFailureResetPassword(String localizedMessage);

    void onSuccessUpdateEmail();
    void onFailureUpdateEmail(String localizedMessage);

    void onSuccessUpdatePassword();
    void onFailureUpdatePassword(String localizedMessage);

    void onSuccessWriteDatabase();
    void onFailureWriteDatabase(String localizedMessage);

    void onSuccessReadDatabase(User user);
    void onFailureReadDatabase(String localizedMessage);

    void onSuccessLogout();

    void onSuccessSetPhoto(Uri uri);
    void onFailureSetPhoto(String localizedMessage);
}
