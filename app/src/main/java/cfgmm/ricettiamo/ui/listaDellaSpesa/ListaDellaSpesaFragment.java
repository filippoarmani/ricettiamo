package cfgmm.ricettiamo.ui.listaDellaSpesa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentListadellaspesaBinding;

public class ListaDellaSpesaFragment extends Fragment {

    private FragmentListadellaspesaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListaDellaSpesaViewModel listaDellaSpesaViewModel =
                new ViewModelProvider(this).get(ListaDellaSpesaViewModel.class);

        binding = FragmentListadellaspesaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        listaDellaSpesaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}