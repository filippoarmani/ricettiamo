package cfgmm.ricettiamo.data.source.user;

import cfgmm.ricettiamo.data.repository.user.IUserResponseCallback;
import cfgmm.ricettiamo.model.User;

public abstract class BaseDatabaseDataSource {

    IUserResponseCallback userResponseCallBack;

    public void setCallBack(IUserResponseCallback userResponseCallBack) {
        this.userResponseCallBack = userResponseCallBack;
    }

    public abstract void writeUser(User user);
    public abstract void readUser(String id);
}
