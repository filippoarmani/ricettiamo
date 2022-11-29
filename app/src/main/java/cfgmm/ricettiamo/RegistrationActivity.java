package cfgmm.ricettiamo;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e_dataNascita.getEditText().setText(dateFormat.format(myCalendar.getTime()));
    }
}