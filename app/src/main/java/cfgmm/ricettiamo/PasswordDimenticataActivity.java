package cfgmm.ricettiamo;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button cancel;
    private Button reset;

    private TextInputLayout email_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);
        mAuth = FirebaseAuth.getInstance();

        cancel = findViewById(R.id.pd_cancel);
        reset = findViewById(R.id.pd_reset);
        email_layout = findViewById(R.id.pd_email_layout);

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        reset.setOnClickListener(v -> {
            String email = email_layout.getEditText().getText().toString().trim();

            if (!isEmpty(email)) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "resetEmail:success");
                                Toast.makeText(PasswordDimenticataActivity.this, "Email inviata con successo.",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.w(TAG, "resetEmail:failure", task.getException());
                                Toast.makeText(PasswordDimenticataActivity.this, "Errore invio email! Riprova",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}