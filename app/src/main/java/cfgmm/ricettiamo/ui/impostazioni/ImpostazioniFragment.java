package cfgmm.ricettiamo.ui.impostazioni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentImpostazioniBinding;

public class ImpostazioniFragment extends Fragment {

    private FragmentImpostazioniBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ImpostazioniViewModel impostazioniViewModel =
                new ViewModelProvider(this).get(ImpostazioniViewModel.class);

        binding = FragmentImpostazioniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        impostazioniViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}