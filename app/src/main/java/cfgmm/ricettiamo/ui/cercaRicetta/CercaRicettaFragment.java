package cfgmm.ricettiamo.ui.cercaRicetta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentCercaricettaBinding;

public class CercaRicettaFragment extends Fragment {

    private FragmentCercaricettaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CercaRicettaViewModel cercaRicettaViewModel =
                new ViewModelProvider(this).get(CercaRicettaViewModel.class);

        binding = FragmentCercaricettaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        cercaRicettaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}