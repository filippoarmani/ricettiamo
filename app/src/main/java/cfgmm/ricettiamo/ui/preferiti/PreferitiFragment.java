package cfgmm.ricettiamo.ui.preferiti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentPreferitiBinding;

public class PreferitiFragment extends Fragment {

    private FragmentPreferitiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PreferitiViewModel preferitiViewModel =
                new ViewModelProvider(this).get(PreferitiViewModel.class);

        binding = FragmentPreferitiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        preferitiViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}