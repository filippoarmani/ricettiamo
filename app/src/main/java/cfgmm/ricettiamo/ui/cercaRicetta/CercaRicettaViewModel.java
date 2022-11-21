package cfgmm.ricettiamo.ui.cercaRicetta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CercaRicettaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CercaRicettaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is CercaRicetta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}