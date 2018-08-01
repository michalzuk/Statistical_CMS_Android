package io.michalzuk.horton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    Button loginButton;
    ProgressBar loginProgressBar;
    EditText loginEmail;
    EditText loginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loginButton = findViewById(R.id.login_button);
        loginProgressBar = findViewById(R.id.login_progressbar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        loginEmail = findViewById(R.id.login_mail);
        loginPassword = findViewById(R.id.login_password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void registerUser() {
        String email = loginEmail.getText().toString().toLowerCase().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError(getString(R.string.email_required));
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPassword.setError(getString(R.string.password_required));
        }
    }
}
