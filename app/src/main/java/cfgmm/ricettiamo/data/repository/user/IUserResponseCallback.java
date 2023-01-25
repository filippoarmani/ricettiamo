package cfgmm.ricettiamo.data.repository.user;

import android.net.Uri;

import cfgmm.ricettiamo.model.User;

public interface IUserResponseCallback {
    void onSuccessRegistration(User newUser);
    void onFailureRegistration(int idError);

    void onSuccessLogin(String uid);
    void onFailureLogin(int idError);

    void onSuccessUpdateEmail();
    void onFailureUpdateEmail(int idError);

    void onSuccessUpdatePassword();
    void onFailureUpdatePassword(int idError);

    void onSuccessWriteDatabase();
    void onFailureWriteDatabase(int idError);

    void onSuccessReadDatabase(User user);
    void onFailureReadDatabase(int idError);

    void onSuccessLogout();

    void onSuccessGetPhoto(Uri uri);
    void onFailureGetPhoto(int idError);

    void onSuccessSetPhoto(Uri uri);
    void onFailureSetPhoto(int idError);

    void onSuccessGetTopTen(User[] topTen);
    void onFailureGetTopTen(int idError);

    void onSuccessGetPosition(int i);
    void onFailureGetPosition(int idError);

    void onSuccessDelete();
    void onFailureDelete();

    void onSuccessUpdateDatabase();
    void onFailureUpdateDatabase(int idError);
}
