package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.app.ProgressDialog;
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

import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.Map;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentSettingsBinding;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class SettingsFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentSettingsBinding binding;
    private Map<String, Object> newInfo;

    private Boolean changed;
    private Uri photoProfile;

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

        userViewModel.getCurrentPhotoLiveData().observe(getViewLifecycleOwner(), photo -> {
            binding.changeUserPhoto.setImageURI(photo);
            photoProfile = photo;
        });

        binding.changeUserPhoto.setOnClickListener(v -> mGetContent.launch("image/*"));

        binding.salva.setOnClickListener(v -> {
            ProgressDialog progress = new ProgressDialog(v.getContext());
            progress.setTitle(R.string.progress_title);
            progress.setMessage(String.valueOf(R.string.progress_message));
            progress.setCancelable(false);
            progress.show();

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
                    if(userViewModel.strongPassword(nPassword1)) {
                        userViewModel.updatePassword(cPassword, nPassword1);
                    }
                }
            }

            if(!(isEmpty(cEmail) || isEmpty(nEmail1) || isEmpty(nEmail2))) {
                if(nEmail1.equals(nEmail2)) {
                    if(EmailValidator.getInstance().isValid(nEmail1)) {
                        newInfo.put("email", nEmail1);
                    }
                }
            }

            if(newInfo.size() > 0) {
                userViewModel.updateData(newInfo);
            }
            progress.dismiss();
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        binding.changeUserPhoto.setImageURI(uri);
                        photoProfile = uri;
                        changed = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

}