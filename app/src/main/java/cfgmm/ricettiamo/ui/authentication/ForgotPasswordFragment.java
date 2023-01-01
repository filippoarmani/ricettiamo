package cfgmm.ricettiamo.ui.authentication;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private TextInputLayout email_layout;

    private UserViewModel userViewModel;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        return new ForgotPasswordFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button cancel = view.findViewById(R.id.pd_cancel);
        Button reset = view.findViewById(R.id.pd_reset);
        email_layout = view.findViewById(R.id.pd_email_layout);

        cancel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        });

        reset.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();

            if (!isEmpty(email)) {
                userViewModel.resetPassword(email);
            }
        });
    }
}