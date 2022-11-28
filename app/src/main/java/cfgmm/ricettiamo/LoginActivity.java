package cfgmm.ricettiamo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button forgot_p;
    private Button login;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);

        forgot_p = findViewById(R.id.l_passwordD);
        login = findViewById(R.id.l_login);
        sign_in = findViewById(R.id.l_registrati);

        forgot_p.setOnClickListener(v -> {

        });

        login.setOnClickListener(v -> {

        });

        sign_in.setOnClickListener(v -> {

        });

    }
}