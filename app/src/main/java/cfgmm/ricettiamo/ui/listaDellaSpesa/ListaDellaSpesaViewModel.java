package cfgmm.ricettiamo.ui.listaDellaSpesa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListaDellaSpesaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListaDellaSpesaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ListaDellaSpesa fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}