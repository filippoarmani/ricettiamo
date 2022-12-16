package cfgmm.ricettiamo.ui.authentication;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputLayout email_layout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        Button cancel = view.findViewById(R.id.pd_cancel);
        Button reset = view.findViewById(R.id.pd_reset);
        email_layout = view.findViewById(R.id.pd_email_layout);

        cancel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        });

        reset.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();

            if (!isEmpty(email)) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(this.getActivity(), task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "resetEmail:success");
                                Toast.makeText(view.getContext(), "Email inviata con successo.",
                                        Toast.LENGTH_SHORT).show();

                                Navigation.findNavController(v).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                            } else {
                                Log.w(TAG, "resetEmail:failure", task.getException());
                                Toast.makeText(view.getContext(), "Errore invio email! Riprova",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}