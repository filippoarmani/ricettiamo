package cfgmm.ricettiamo;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final Calendar myCalendar= Calendar.getInstance();

    private TextInputLayout e_name;
    private TextInputLayout e_surname;
    private TextInputLayout e_birthDate;
    private TextInputLayout e_email;
    private TextInputLayout e_phoneNumber;
    private TextInputLayout e_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        e_name = findViewById(R.id.r_nome_layout);
        e_surname = findViewById(R.id.r_cognome_layout);
        e_birthDate = findViewById(R.id.r_date_layout);
        e_email = findViewById(R.id.r_email_layout);
        e_phoneNumber = findViewById(R.id.r_phone_layout);
        e_password = findViewById(R.id.r_password_layout);

        Button registration = findViewById(R.id.r_creaAccount);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        e_birthDate.getEditText().setOnClickListener(view ->
                new DatePickerDialog(RegistrationActivity.this,date,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );

        registration.setOnClickListener(v -> {

            String name = e_name.getEditText().getText().toString().trim();
            String surname = e_surname.getEditText().getText().toString().trim();
            String birthDate = e_birthDate.getEditText().getText().toString().trim();
            String phoneNumber = e_phoneNumber.getEditText().getText().toString().trim();

            String email = e_email.getEditText().getText().toString().trim();
            String password = e_password.getEditText().getText().toString().trim();


            if(checkOk(v, name, surname, birthDate, phoneNumber, email, password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(RegistrationActivity.this, R.string.reg_s,
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name + " " + surname)
                                        .setPhotoUri(Uri.parse(String.valueOf(R.drawable.user)))
                                        .build();

                                user.updateProfile(profileUpdates);

                                Toast.makeText(RegistrationActivity.this, R.string.reg_s,
                                        Toast.LENGTH_SHORT).show();

                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, R.string.reg_f,
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
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

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e_birthDate.getEditText().setText(dateFormat.format(myCalendar.getTime()));
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
    private void updateUI(FirebaseUser currentUser) {
        Intent intent;

        if(currentUser == null)
            intent = new Intent(this, RegistrationActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}