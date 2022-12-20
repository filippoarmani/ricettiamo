package cfgmm.ricettiamo.data.source.user;

import java.util.Set;

import cfgmm.ricettiamo.data.repository.IUserResponseCallback;
import cfgmm.ricettiamo.model.User;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected IUserResponseCallback userResponseCallback;

    public void setUserResponseCallback(IUserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
}
