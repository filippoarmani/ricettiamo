package cfgmm.ricettiamo.data.source.user;

import java.util.Map;

import cfgmm.ricettiamo.data.repository.user.IUserResponseCallback;
import cfgmm.ricettiamo.model.User;

public abstract class BaseDatabaseDataSource {

    IUserResponseCallback userResponseCallBack;

    public void setCallBack(IUserResponseCallback userResponseCallBack) {
        this.userResponseCallBack = userResponseCallBack;
    }

    public abstract void writeUser(User user);
    public abstract void readUser(String id);

    public abstract void updateData(Map<String, Object> newInfo, String id);

    public abstract void getTopTen();
    public abstract void getPosition(String id);
}
