package io.michalzuk.horton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.michalzuk.horton.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout loginLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressBar loginProgressBar;
    private EditText editTextLoginEmail, editTextLoginPassword;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        loginLayout = findViewById(R.id.activity_login);
//        firebaseAuth = FirebaseAuth.getInstance();
//        Button loginButton = findViewById(R.id.login_button);
//        Button registrationButton = findViewById(R.id.registration_button);
//        loginProgressBar = findViewById(R.id.login_progressbar);
//        loginProgressBar.setVisibility(View.INVISIBLE);
//        editTextLoginEmail = findViewById(R.id.login_mail);
//        editTextLoginPassword = findViewById(R.id.login_password);
//        firebaseAuth = FirebaseAuth.getInstance();
//
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = editTextLoginEmail.getText().toString().trim();
//                String password = editTextLoginPassword.getText().toString().trim();
//
//                if (TextUtils.isEmpty(email)) {
//                    Snackbar.make(loginLayout, getString(R.string.enter_email_address), Snackbar.LENGTH_SHORT).show();
//                    editTextLoginEmail.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    Snackbar.make(loginLayout, getString(R.string.enter_password), Snackbar.LENGTH_SHORT).show();
//                    editTextLoginPassword.requestFocus();
//                    return;
//                }
//
//                loginProgressBar.setVisibility(View.VISIBLE);
//
//                firebaseAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                loginProgressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Snackbar.make(loginLayout, Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//            }
//        });
//
//        registrationButton.setOnClickListener(new View.OnClickListener() {
//            final Intent registrationIntent = new Intent(LoginActivity.this, SignUpActivity.class);
//
//            @Override
//            public void onClick(View view) {
//                LoginActivity.this.startActivity(registrationIntent);
//            }
//        });
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.registration_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);

        loginProgressBar = findViewById(R.id.login_progressbar);
        editTextLoginEmail = findViewById(R.id.login_email);
        editTextLoginPassword = findViewById(R.id.login_password);
        firebaseAuth = FirebaseAuth.getInstance();
        loginProgressBar.setVisibility(View.INVISIBLE);
        editTextLoginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        editTextLoginEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_button:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.login_button:
                loginUser();
        }
    }

    private void loginUser() {

        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();
        loginProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginProgressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Snackbar.make(loginLayout, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
