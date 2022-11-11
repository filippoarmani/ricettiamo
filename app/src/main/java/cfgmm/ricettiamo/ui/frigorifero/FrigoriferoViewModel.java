package cfgmm.ricettiamo.ui.frigorifero;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FrigoriferoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FrigoriferoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Frigorifero fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}