package cfgmm.ricettiamo.ui.authentication;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;

import cfgmm.ricettiamo.ui.navigation_drawer.MainActivity;
import cfgmm.ricettiamo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private FirebaseAuth mAuth;
    final Calendar myCalendar= Calendar.getInstance();

    private TextInputLayout e_name;
    private TextInputLayout e_surname;
    private TextInputLayout e_birthDate;
    private TextInputLayout e_email;
    private TextInputLayout e_phoneNumber;
    private TextInputLayout e_password;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        e_name = view.findViewById(R.id.r_nome_layout);
        e_surname = view.findViewById(R.id.r_cognome_layout);
        e_birthDate = view.findViewById(R.id.r_date_layout);
        e_email = view.findViewById(R.id.r_email_layout);
        e_phoneNumber = view.findViewById(R.id.r_phone_layout);
        e_password = view.findViewById(R.id.r_password_layout);

        Button registration = view.findViewById(R.id.r_creaAccount);

        registration.setOnClickListener(v -> {

            String name = e_name.getEditText().getText().toString().trim();
            String surname = e_surname.getEditText().getText().toString().trim();
            String birthDate = e_birthDate.getEditText().getText().toString().trim();
            String phoneNumber = e_phoneNumber.getEditText().getText().toString().trim();

            String email = e_email.getEditText().getText().toString().trim();
            String password = e_password.getEditText().getText().toString().trim();


            if(checkOk(v, name, surname, birthDate, phoneNumber, email, password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(v.getContext(), R.string.reg_s,
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name + " " + surname)
                                        .setPhotoUri(Uri.parse(String.valueOf(R.drawable.user)))
                                        .build();

                                user.updateProfile(profileUpdates);

                                Toast.makeText(v.getContext(), R.string.reg_s,
                                        Toast.LENGTH_SHORT).show();

                                updateUI(view, user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(v.getContext(), R.string.reg_f,
                                        Toast.LENGTH_SHORT).show();
                                updateUI(view,null);
                            }
                        });
            }
        });
    }

    private boolean checkOk(View v, String name, String surname, String birthDate, String phoneNumber, String email, String password) {
        if(isEmpty(name) || isEmpty(surname) || isEmpty(birthDate) ||
                isEmpty(phoneNumber) || isEmpty(email) || isEmpty(password)) {
            Snackbar.make(v, R.string.empty_fields, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        if(!EmailValidator.getInstance().isValid(email)) {
            Snackbar.make(v, "Invalid Email", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        if(password.length() < 8) {
            Snackbar.make(v, R.string.short_password, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        int i = 0;
        boolean noUpper = true;
        boolean noLower = true;
        boolean noNumber = true;
        boolean noSpecial = true;

        while(i < password.length() && (noUpper || noLower || noNumber || noSpecial)) {
            char c = password.charAt(i);

            if(c >= 'a' && c <= 'z')
                noLower = false;
            else if(c >= 'A' && c <= 'Z')
                noUpper = false;
            else if(c >= '0' && c <= '0')
                noNumber = false;
            else
                noSpecial = false;

            i++;
        }

        if(noUpper || noLower || noNumber || noSpecial) {
            Snackbar.make(v, R.string.weak_password, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        return true;
    }

    /*
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                currentUser.reload();
            }
        }
    */
    private void updateUI(View v, FirebaseUser currentUser) {

        if (currentUser == null)
            Navigation.findNavController(v).navigate(R.id.action_registrationFragment_self);
        else {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

    }
}