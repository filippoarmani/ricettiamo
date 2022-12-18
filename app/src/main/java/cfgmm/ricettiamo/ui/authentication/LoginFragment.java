package cfgmm.ricettiamo.ui.authentication;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.ui.navigation_drawer.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private TextInputLayout email_layout;
    private TextInputLayout password_layout;

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        email_layout = view.findViewById(R.id.pd_email_layout);
        password_layout = view.findViewById(R.id.l_password_layout);

        Button login_password = view.findViewById(R.id.l_login);
        Button login_google = view.findViewById(R.id.button_Login_Google);
        TextView f_password = view.findViewById(R.id.l_forgotPassword);
        Button login_later = view.findViewById(R.id.btn_noLogIn);
        TextView sign_in = view.findViewById(R.id.l_registration);

        login_password.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();
            String password = password_layout.getEditText().getText().toString().trim();

            if (!(isEmpty(email) || isEmpty(password))) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this.getActivity(), task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");

                                Toast.makeText(view.getContext(), R.string.auth_s,
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(view, user);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                                Toast.makeText(view.getContext(), R.string.auth_f,
                                        Toast.LENGTH_SHORT).show();

                                updateUI(view,null);
                            }
                        });
            } else {
                Snackbar.make(v, R.string.empty_fields, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        f_password.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        });

        login_later.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        });

        sign_in.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment);
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();

        googleSignInClient = GoogleSignIn.getClient(this.getActivity(), googleSignInOptions);

        login_google.setOnClickListener(v -> {
            Log.d(TAG, "onClick: begin Google SignIn");
            Intent intent = googleSignInClient.getSignInIntent();
            intent.putExtra("RC_SIGN_IN", RC_SIGN_IN);
            startActivity(intent);
        });
    }

    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(getView(), currentUser);
        }
    }

    private void updateUI(View v, FirebaseUser currentUser) {

        if (currentUser == null)
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_self);
        else {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google Signin intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (Exception e) {
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Log.d(TAG, "onSuccess: Logged In");

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                })
                .addOnFailureListener(e ->
                        Log.d(TAG, "onFailure: Loggin failed " + e.getMessage())
                )
        ;
    }
}