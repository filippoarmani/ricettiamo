package cfgmm.ricettiamo.ui.authentication;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

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
        return inflater.inflate(R.layout.fragment_a_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button reset = view.findViewById(R.id.pd_reset);
        email_layout = view.findViewById(R.id.pd_email_layout);

        reset.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();

            if (!isEmpty(email)) {
                if(EmailValidator.getInstance().isValid(email))
                    userViewModel.resetPassword(email);
                else
                    email_layout.setError(getString(R.string.invalid_email));
            }
        });
    }
}