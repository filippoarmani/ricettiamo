package cfgmm.ricettiamo;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final Calendar myCalendar= Calendar.getInstance();

    private TextInputLayout e_nome;
    private TextInputLayout e_cognome;
    private TextInputLayout e_dataNascita;
    private TextInputLayout e_email;
    private TextInputLayout e_phoneNumber;
    private TextInputLayout e_password;

    private Button registrati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        mAuth = FirebaseAuth.getInstance();

        e_nome = findViewById(R.id.r_nome_layout);
        e_cognome = findViewById(R.id.r_cognome_layout);
        e_dataNascita = findViewById(R.id.r_date_layout);
        e_email = findViewById(R.id.r_email_layout);
        e_phoneNumber = findViewById(R.id.r_phone_layout);
        e_password = findViewById(R.id.r_password_layout);

        registrati = findViewById(R.id.r_creaAccount);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        e_dataNascita.getEditText().setOnClickListener(view ->
                new DatePickerDialog(RegistrationActivity.this,date,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );

        registrati.setOnClickListener(v -> {

            String nome = e_nome.getEditText().getText().toString().trim();
            String cognome = e_cognome.getEditText().getText().toString().trim();
            String dataNascita = e_dataNascita.getEditText().getText().toString().trim();
            String phoneNumber = e_phoneNumber.getEditText().getText().toString().trim();

            String email = e_email.getEditText().getText().toString().trim();
            String password = e_password.getEditText().getText().toString().trim();

            if(checkOk(nome, cognome, dataNascita, phoneNumber, email, password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });
            }  else {
                Snackbar.make(v, "The fields must not be empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });
    }

    private boolean checkOk(String nome, String cognome, String dataNascita, String phoneNumber, String email, String password) {
        if(isEmpty(nome) ||
                isEmpty(cognome) ||
                isEmpty(dataNascita) ||
                isEmpty(phoneNumber) ||
                isEmpty(email) ||
            isEmpty(password))
            return false;

        return true;
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e_dataNascita.getEditText().setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

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