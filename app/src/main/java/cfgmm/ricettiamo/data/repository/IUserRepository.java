package cfgmm.ricettiamo.data.repository;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;

public interface IUserRepository {

    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();

    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);

}
