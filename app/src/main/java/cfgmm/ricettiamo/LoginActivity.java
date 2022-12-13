package cfgmm.ricettiamo;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email_layout;
	private TextInputLayout password_layout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email_layout = findViewById(R.id.pd_email_layout);
        password_layout = findViewById(R.id.l_password_layout);

        Button login_password = findViewById(R.id.l_login);
        Button login_google = findViewById(R.id.button_Login_Google);
        TextView f_password = findViewById(R.id.l_forgotPassword);
        Button login_later = findViewById(R.id.btn_noLogIn);
        TextView sign_in = findViewById(R.id.l_registration);

        login_password.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();
            String password = password_layout.getEditText().getText().toString().trim();

            if (!(isEmpty(email) || isEmpty(password))) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");

                                Toast.makeText(LoginActivity.this, R.string.auth_s,
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                                Toast.makeText(LoginActivity.this, R.string.auth_f,
                                        Toast.LENGTH_SHORT).show();

                                updateUI(null);
                            }
                        });
            } else {
                Snackbar.make(v, R.string.empty_fields, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        f_password.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        login_later.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        login_google.setOnClickListener(v -> {

        });
    }

    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent;

        if (currentUser == null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}