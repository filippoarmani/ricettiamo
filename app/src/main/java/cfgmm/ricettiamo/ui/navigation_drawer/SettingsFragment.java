package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentSettingsBinding;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.ui.authentication.AuthenticationActivity;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class SettingsFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentSettingsBinding binding;
    private Map<String, Object> newInfo;

    private Boolean changed;
    private Uri photoProfile;
    private final String[] image = {"image/*"};

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param2) {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        newInfo = new HashMap<>();
        photoProfile = Uri.parse(String.valueOf(R.drawable.user));
        changed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        CircularProgressIndicator progressIndicator = view.findViewById(R.id.s_progress_circular);

        userViewModel.getCurrentPhotoLiveData().observe(getViewLifecycleOwner(), result -> {
            progressIndicator.setVisibility(View.VISIBLE);
            if(result.isSuccess()) {
                Uri photo = ((Result.PhotoResponseSuccess) result).getData();
                try {
                    Glide.with(this)
                            .load(photo)
                            .circleCrop()
                            .into(binding.changeUserPhoto);
                } catch (Exception e) {

                }
                photoProfile = photo;
            }
            progressIndicator.setVisibility(View.GONE);
        });

        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
            progressIndicator.setVisibility(View.VISIBLE);
            if(result.isSuccess()) {
                User user = ((Result.UserResponseSuccess) result).getData();
                if(userViewModel.isLoggedUser()){
                    if (user.getSignInMethod().equals("google")) {
                        binding.noGoogle.setVisibility(View.GONE);
                        binding.warning.setVisibility(View.VISIBLE);
                    } else {
                        binding.noGoogle.setVisibility(View.VISIBLE);
                        binding.warning.setVisibility(View.GONE);
                    }
                } else {
                    Intent intent = new Intent(requireContext() , AuthenticationActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
            progressIndicator.setVisibility(View.GONE);
        });

        binding.changeUserPhoto.setOnClickListener(v -> {
            mGetContent.launch(image);
        });

        binding.salva.setOnClickListener(v -> {
            String displayName = binding.iDName.getText().toString().trim();

            String description = binding.description.getText().toString().trim();

            String cPassword = binding.iCPassword.getText().toString().trim();
            String nPassword1 = binding.iNPassword1.getText().toString().trim();
            String nPassword2 = binding.iNPassword2.getText().toString().trim();

            String cEmail = binding.iCEmail.getText().toString().trim();
            String nEmail1 = binding.iNEmail1.getText().toString().trim();
            String nEmail2 = binding.iNEmail2.getText().toString().trim();

            if(changed) {
                userViewModel.updatePhoto(photoProfile);
                changed = false;
            }

            if(!isEmpty(displayName)) {
                newInfo.put("displayName", displayName);
            }

            if(!isEmpty(description)) {
                newInfo.put("description", description);
            }

            if(!(isEmpty(cPassword) || isEmpty(nPassword1) || isEmpty(nPassword2))) {
                if(nPassword1.equals(nPassword2)) {
                    if(userViewModel.strongPassword(nPassword1) && !cPassword.equals(nPassword1)) {
                        userViewModel.updatePassword(cPassword, nPassword1);
                    }
                } else {

                    binding.iNPassword1Layout.setError(getResources().getString(R.string.field_not_match));
                    binding.iNPassword2Layout.setError(getResources().getString(R.string.field_not_match));
                }
            }

            if(!(isEmpty(cEmail) || isEmpty(nEmail1) || isEmpty(nEmail2))) {
                if(nEmail1.equals(nEmail2)) {
                    if(EmailValidator.getInstance().isValid(nEmail1) && !cEmail.equals(nEmail1)) {
                        newInfo.put("email", nEmail1);
                    }
                } else {
                    binding.iNEmail1Layout.setError(getResources().getString(R.string.field_not_match));
                    binding.iNEmail2Layout.setError(getResources().getString(R.string.field_not_match));
                }
            }

            if(newInfo.size() > 0) {
                userViewModel.updateData(newInfo);
            }

            Result result = userViewModel.getCurrentUserLiveData().getValue();
            if(!result.isSuccess()) {
                Result.Error error = (Result.Error) result;
                Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_LONG);
            }
        });

        binding.logout.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                        userViewModel.signOut();
                        dialog.cancel();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                        dialog.cancel();
                    }).create().show();
        });
    }


    ActivityResultLauncher<String[]> mGetContent = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        Glide.with(requireContext())
                                .load(uri)
                                .circleCrop()
                                .into(binding.changeUserPhoto);
                        photoProfile = uri;
                        changed = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

}