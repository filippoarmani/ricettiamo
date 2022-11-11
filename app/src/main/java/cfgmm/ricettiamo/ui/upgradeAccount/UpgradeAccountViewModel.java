package cfgmm.ricettiamo.ui.upgradeAccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpgradeAccountViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UpgradeAccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}