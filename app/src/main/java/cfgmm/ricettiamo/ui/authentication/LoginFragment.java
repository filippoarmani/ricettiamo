package cfgmm.ricettiamo.ui.authentication;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.ui.navigation_drawer.MainActivity;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class LoginFragment extends Fragment {

    private TextInputLayout email_layout;
    private TextInputLayout password_layout;

    private UserViewModel userViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        email_layout = view.findViewById(R.id.pd_email_layout);
        password_layout = view.findViewById(R.id.l_password_layout);

        Button login_password = view.findViewById(R.id.l_login);
        Button login_google = view.findViewById(R.id.button_Login_Google);
        TextView f_password = view.findViewById(R.id.l_forgotPassword);
        Button login_later = view.findViewById(R.id.btn_noLogIn);
        TextView sign_up = view.findViewById(R.id.l_registration);

        login_password.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();
            String password = password_layout.getEditText().getText().toString().trim();

            if (!(isEmpty(email) || isEmpty(password))) {
                userViewModel.signIn(email, password);
                updateUI();
            } else {

            }
        });

        f_password.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        login_later.setOnClickListener(v -> updateUI());

        sign_up.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment));

        login_google.setOnClickListener(v -> {

        });
    }

    public void onStart() {
        super.onStart();
    }

    private void updateUI() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        requireActivity().startActivity(intent);
        requireActivity().finish();
    }

}