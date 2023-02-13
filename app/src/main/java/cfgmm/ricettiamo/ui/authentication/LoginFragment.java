package cfgmm.ricettiamo.ui.authentication;

import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

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

    private CircularProgressIndicator progressIndicator;

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

    private TextInputLayout email_layout;
    private TextInputLayout password_layout;

    private UserViewModel userViewModel;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

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

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getResources().getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                Log.d(TAG, "result.getResultCode() == Activity.RESULT_OK");
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        userViewModel.signInGoogle(idToken);

                        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
                            progressIndicator.setVisibility(View.GONE);
                            if(result.isSuccess()) {
                                updateUI(userViewModel.isLoggedUser());
                            } else {
                                if(!userViewModel.isLoggedUser()) {
                                    Result.Error error = (Result.Error) result;
                                    Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
        Button login_google = view.findViewById(R.id.button_Login_Google);
        Button f_password = view.findViewById(R.id.l_forgotPassword);
        Button login_later = view.findViewById(R.id.btn_noLogIn);
        Button sign_up = view.findViewById(R.id.l_registration);

        progressIndicator = view.findViewById(R.id.l_progress_circular);

        login_password.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            String email = email_layout.getEditText().getText().toString().trim();
            String password = password_layout.getEditText().getText().toString().trim();

            if (!(isEmpty(email) || isEmpty(password))) {
                userViewModel.signIn(email, password);
                Log.e("user 1 ", String.valueOf(userViewModel.isLoggedUser()));
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
                builder.setMessage(R.string.confirmation)
                        .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                            userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
                                progressIndicator.setVisibility(View.GONE);
                                Log.e("user 2 ", String.valueOf(userViewModel.isLoggedUser()));
                                if(result.isSuccess()) {
                                    Log.e("user 3 ", String.valueOf(userViewModel.isLoggedUser()));
                                    updateUI(userViewModel.isLoggedUser());
                                } else {
                                    if(!userViewModel.isLoggedUser()) {
                                        Log.e("user 4 ", String.valueOf(userViewModel.isLoggedUser()));
                                        Result.Error error = (Result.Error) result;
                                        Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.cancel();
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel()).create().show();

            } else {
                Snackbar.make(requireView(), R.string.empty_fields, Snackbar.LENGTH_SHORT).show();
                progressIndicator.setVisibility(View.GONE);
            }
        });

        f_password.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        login_later.setOnClickListener(v -> updateUI(true));

        sign_up.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment));

        login_google.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity(), result -> {
                        Log.d(TAG, "onSuccess from oneTapClient.beginSignIn(BeginSignInRequest)");
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                        activityResultLauncher.launch(intentSenderRequest);

                    })
                    .addOnFailureListener(requireActivity(), e -> {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.error_no_google_account_found_message),
                                Snackbar.LENGTH_SHORT).show();
                        progressIndicator.setVisibility(View.GONE);
                    });
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