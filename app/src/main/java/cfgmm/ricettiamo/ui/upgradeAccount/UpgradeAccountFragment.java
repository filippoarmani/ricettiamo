package cfgmm.ricettiamo.ui.upgradeAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.databinding.FragmentUpgradeaccountBinding;

public class UpgradeAccountFragment extends Fragment {

    private FragmentUpgradeaccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UpgradeAccountViewModel upgradeAccountViewModel =
                new ViewModelProvider(this).get(UpgradeAccountViewModel.class);

        binding = FragmentUpgradeaccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        upgradeAccountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}