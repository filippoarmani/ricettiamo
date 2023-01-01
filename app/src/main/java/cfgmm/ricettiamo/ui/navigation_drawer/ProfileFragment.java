package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentProfileBinding;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class ProfileFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.recipeCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_my_recipe));
        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), user -> {
            binding.user.setImageURI(Uri.parse(user.getPhoto()));

            String userFullName = user.getName() + " " + user.getSurname();
            binding.fullName.setText(userFullName);
            binding.displayName.setText(user.getDisplayName());
            binding.email.setText(user.getEmail());

            String userDescription = user.getDescription();
            if(isEmpty(userDescription)) {
                binding.descriptionCardView.setVisibility(GONE);
                binding.description.setVisibility(GONE);
            } else {
                binding.descriptionCardView.setVisibility(VISIBLE);
                binding.description.setVisibility(VISIBLE);
                binding.description.setText(userDescription);
            }

            binding.totalStars.setText("" + user.getTotalStars());
            binding.position.setText("" + user.getPositions());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}