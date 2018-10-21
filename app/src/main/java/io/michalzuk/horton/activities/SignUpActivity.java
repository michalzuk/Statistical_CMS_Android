package io.michalzuk.horton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import io.michalzuk.horton.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private RelativeLayout registerLayout;
    private ProgressBar loginProgressBar;
    private EditText editTextLoginEmail, editTextLoginPassword;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        registerLayout = findViewById(R.id.sign_up_layout);
        editTextLoginEmail = findViewById(R.id.login_email);
        editTextLoginPassword = findViewById(R.id.login_password);
        loginProgressBar = findViewById(R.id.login_progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.create_new_account).setOnClickListener(this);
        loginProgressBar.setVisibility(View.INVISIBLE);

        editTextLoginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        editTextLoginEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_new_account:
                registerUser();
                break;

            case R.id.back_to_login:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    private void registerUser() {
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();

        loginProgressBar.setVisibility(View.VISIBLE);

        if (email.isEmpty()) {
            editTextLoginEmail.setError("Email is required");
            editTextLoginEmail.requestFocus();
        } else if (password.isEmpty()) {
            editTextLoginPassword.setError("Password is required");
            editTextLoginPassword.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextLoginPassword.setError("Please enter a valid email");
            editTextLoginEmail.requestFocus();
        } else if (password.length() < 6) {
            editTextLoginPassword.setError("Minimum password length is equal to 6");
            editTextLoginPassword.requestFocus();
        } else {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                finish();
                                Snackbar.make(registerLayout, getString(R.string.successfully_registred), Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            } else if (task.isCanceled()) {
                                Snackbar.make(registerLayout, getString(R.string.registration_canceled), Snackbar.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Snackbar.make(registerLayout, getString(R.string.already_registered), Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(registerLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
