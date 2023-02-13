package cfgmm.ricettiamo.ui.authentication;

import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.ui.navigation_drawer.MainActivity;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private static final int RC_SIGN_IN = 1;

    private LinearProgressIndicator progressIndicator;

    private TextInputLayout email_layout;
    private TextInputLayout password_layout;

    private GoogleSignInClient mGoogleApiClient;
    private ActivityResultLauncher<Intent> googleActivityResultLauncher;

    private UserViewModel userViewModel;

    private Button login_google;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultA -> {
                    try {
                        if (resultA.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = resultA.getData();
                            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                            if (result.isSuccess()) {
                                // Google Sign In was successful, authenticate with Firebase
                                GoogleSignInAccount account = result.getSignInAccount();
                                if(account != null) {

                                    userViewModel.signInGoogle(account.getIdToken());

                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        Snackbar.make(requireView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }

                                    userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result2 -> {
                                        progressIndicator.setVisibility(View.GONE);
                                        if (result2.isSuccess()) {
                                            updateUI(userViewModel.isLoggedUser());
                                        } else {
                                            if (!userViewModel.isLoggedUser()) {
                                                Result.Error error = (Result.Error) result2;
                                                Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Snackbar.make(requireView(), R.string.unexpected_error, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Snackbar.make(requireView(), R.string.unexpected_error, Snackbar.LENGTH_SHORT).show();
                    }
                });

        mGoogleApiClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        email_layout = view.findViewById(R.id.pd_email_layout);
        password_layout = view.findViewById(R.id.l_password_layout);

        Button login_password = view.findViewById(R.id.l_login);
        login_google = view.findViewById(R.id.button_Login_Google);
        Button f_password = view.findViewById(R.id.l_forgotPassword);
        Button login_later = view.findViewById(R.id.btn_noLogIn);
        Button sign_up = view.findViewById(R.id.l_registration);

        progressIndicator = view.findViewById(R.id.progress);

        login_password.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            String email = email_layout.getEditText().getText().toString().trim();
            String password = password_layout.getEditText().getText().toString().trim();

            if (!(isEmpty(email) || isEmpty(password))) {
                userViewModel.signIn(email, password);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Snackbar.make(requireView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }

                userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
                    if(result.isSuccess()) {
                        updateUI(userViewModel.isLoggedUser());
                    } else {
                        if(!userViewModel.isLoggedUser()) {
                            Result.Error error = (Result.Error) result;
                            Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Snackbar.make(requireView(), R.string.empty_fields, Snackbar.LENGTH_SHORT).show();
            }
            progressIndicator.setVisibility(View.GONE);
        });

        f_password.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        login_later.setOnClickListener(v -> updateUI(true));

        sign_up.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment));

        login_google.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            Intent signInIntent = mGoogleApiClient.getSignInIntent();
            googleActivityResultLauncher.launch(signInIntent);
            progressIndicator.setVisibility(View.GONE);
        });
    }

    private void updateUI(boolean start) {
        if(start) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addCategory(Intent.ACTION_OPEN_DOCUMENT);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(userViewModel.isLoggedUser());
    }
}