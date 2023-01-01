package cfgmm.ricettiamo.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.User;

public interface IUserRepository {

    void signUp(User newUser, String email, String password);
    void signIn(String email, String password);

    void resetPassword(String email);
    void updateEmail(String email);
    void updatePassword(String oldPassword, String newPassword);
    MutableLiveData<User> getLoggedUser();

    boolean isLoggedUser();
}
