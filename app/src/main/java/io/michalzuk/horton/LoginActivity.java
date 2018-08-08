package io.michalzuk.horton;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.michalzuk.horton.activities.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout loginLayout;
    private FirebaseAuth firebaseAuth;
    private Button loginButton, registrationButton;
    private ProgressBar loginProgressBar;
    private EditText editTextLoginEmail, editTextLoginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginLayout = findViewById(R.id.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.login_button);
        registrationButton = findViewById(R.id.registration_button);
        loginProgressBar = findViewById(R.id.login_progressbar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        editTextLoginEmail = findViewById(R.id.login_mail);
        editTextLoginPassword = findViewById(R.id.login_password);
        firebaseAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextLoginEmail.getText().toString().trim();
                String password = editTextLoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(loginLayout, getString(R.string.enter_email_address), Snackbar.LENGTH_SHORT).show();
                    editTextLoginEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(loginLayout, getString(R.string.enter_password), Snackbar.LENGTH_SHORT).show();
                    editTextLoginPassword.requestFocus();
                    return;
                }

                loginProgressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loginProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Snackbar.make(loginLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            Intent registrationIntent = new Intent(LoginActivity.this, SignUpActivity.class);

            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(registrationIntent);
            }
        });
    }


}
