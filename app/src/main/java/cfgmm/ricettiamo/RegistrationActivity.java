package cfgmm.ricettiamo;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class RegistrationActivity extends AppCompatActivity {

    private EditText e_nome;
    private EditText e_cognome;
    private EditText e_dataNascita;
    private EditText e_email;
    private EditText e_phoneNumber;
    private EditText e_password;

    private Button registrati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        e_nome = findViewById(R.id.r_nome);
        e_cognome = findViewById(R.id.r_cognome);
        e_dataNascita = findViewById(R.id.r_dataNascita);
        e_email = findViewById(R.id.r_email);
        e_phoneNumber = findViewById(R.id.r_telefono);
        e_password = findViewById(R.id.r_password);

        registrati = findViewById(R.id.r_creaAccount);

        registrati.setOnClickListener(v -> {
            String nome = e_nome.getText().toString();
            String cognome = e_cognome.getText().toString();
            String dataNascita = e_dataNascita.getText().toString();
            String email = e_email.getText().toString();
            String numero = e_phoneNumber.getText().toString();
            String password = e_password.getText().toString();

            if(isEmpty(nome) || isEmpty(cognome) || isEmpty(dataNascita) || isEmpty(email) || isEmpty(numero) || isEmpty(password)) {
                Snackbar.make(v, "The fields must not be empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            if(!isDataOk(dataNascita)) {
                Snackbar.make(v, "Wrong Date Format", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private boolean isDataOk(String dataNascita) {
        return true;
    }
}