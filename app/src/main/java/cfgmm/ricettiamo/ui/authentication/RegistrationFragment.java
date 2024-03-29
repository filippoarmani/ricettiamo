package cfgmm.ricettiamo.ui.authentication;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.concurrent.TimeUnit;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.ui.navigation_drawer.MainActivity;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class RegistrationFragment extends Fragment {

    private UserViewModel userViewModel;

    private TextInputLayout e_name;
    private TextInputLayout e_surname;
    private TextInputLayout e_email;
    private TextInputLayout e_password;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
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
        return inflater.inflate(R.layout.fragment_a_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        e_name = view.findViewById(R.id.r_nome_layout);
        e_surname = view.findViewById(R.id.r_cognome_layout);
        e_email = view.findViewById(R.id.r_email_layout);
        e_password = view.findViewById(R.id.r_password_layout);
        LinearProgressIndicator linear_pb = view.findViewById(R.id.progress_bar_r);

        Button registration = view.findViewById(R.id.r_creaAccount);

        registration.setOnClickListener(v -> {
            linear_pb.setVisibility(View.VISIBLE);

            String name = e_name.getEditText().getText().toString().trim();
            String surname = e_surname.getEditText().getText().toString().trim();
            String email = e_email.getEditText().getText().toString().trim();
            String password = e_password.getEditText().getText().toString().trim();


            if(checkOk(name, surname, email, password)) {
                User newUser = new User(
                        null,
                        null,
                        name + " " + surname,
                        email
                );
                userViewModel.signUp(newUser, email, password);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Snackbar.make(requireView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }

                Result result = userViewModel.getCurrentUserLiveData().getValue();
                if(userViewModel.isLoggedUser()) {
                    updateUI();
                } else {
                    if(result != null && !result.isSuccess()) {
                        Result.Error error = (Result.Error) result;
                        Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            linear_pb.setVisibility(View.GONE);
        });
    }

    private boolean checkOk(String name, String surname, String email, String password) {
        if(isEmpty(name) || isEmpty(surname) || isEmpty(email) || isEmpty(password)) {
            Snackbar.make(requireView(), R.string.empty_fields, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(!EmailValidator.getInstance().isValid(email)) {
            Snackbar.make(requireView(), getString(R.string.invalid_email), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return userViewModel.strongPassword(password);
    }

    private void updateUI() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addCategory(Intent.ACTION_OPEN_DOCUMENT);
        requireActivity().startActivity(intent);
        requireActivity().finish();
    }
}