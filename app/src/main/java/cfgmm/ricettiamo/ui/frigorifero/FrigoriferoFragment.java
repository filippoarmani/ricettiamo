package cfgmm.ricettiamo.ui.frigorifero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentFrigoriferoBinding;
import cfgmm.ricettiamo.ui.frigorifero.FrigoriferoViewModel;

public class FrigoriferoFragment extends Fragment {

    private FragmentFrigoriferoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FrigoriferoViewModel frigoriferoViewModel =
                new ViewModelProvider(this).get(FrigoriferoViewModel.class);

        binding = FragmentFrigoriferoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        frigoriferoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}