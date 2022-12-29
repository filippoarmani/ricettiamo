package cfgmm.ricettiamo.data.repository.user;

import cfgmm.ricettiamo.model.User;

public interface IUserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}
