package cfgmm.ricettiamo.data.source.user;

import cfgmm.ricettiamo.data.repository.user.IUserResponseCallback;
import cfgmm.ricettiamo.model.User;

/**
 * Base class to manage the user authentication.
 */
public abstract class BaseUserAuthenticationRemoteDataSource {
    protected IUserResponseCallback userResponseCallback;

    public void setUserResponseCallback(IUserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
}
