package io.michalzuk.horton;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private ProgressBar loginProgressBar;
    private EditText editTextLoginUsername, editTextLoginEmail, editTextLoginPassword;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        relativeLayout = findViewById(R.id.sign_up_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        Button createAccount = findViewById(R.id.create_new_account);
        Button backToLogin = findViewById(R.id.back_to_login);
        loginProgressBar = findViewById(R.id.login_progressbar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        editTextLoginEmail = findViewById(R.id.login_mail);
        editTextLoginPassword = findViewById(R.id.login_password);
        editTextLoginUsername = findViewById(R.id.login_username);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            Intent backToLoginIntent = new Intent(SignUpActivity.this, LoginActivity.class);

            @Override
            public void onClick(View view) {
                SignUpActivity.this.startActivity(backToLoginIntent);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.w("Firebase", "User already logged in: " + firebaseAuth.getCurrentUser());
        }
    }

    private void registerUser() {
        final String email = editTextLoginEmail.getText().toString().toLowerCase().trim();
        final String password = editTextLoginPassword.getText().toString().trim();
        final String username = editTextLoginUsername.getText().toString().toLowerCase();

        if (email.isEmpty()) {
            editTextLoginEmail.setError(getString(R.string.error_email_required));
            editTextLoginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextLoginEmail.setError(getString(R.string.error_email_invalid));
            return;
        }

        if (password.isEmpty()) {
            editTextLoginPassword.setError(getString(R.string.password_required));
            editTextLoginPassword.requestFocus();
            return;
        }

        if (password.length() <= 8) {
            editTextLoginPassword.setError(getString(R.string.password_length_incorrect));
            editTextLoginPassword.requestFocus();
            return;
        }

        if (username.length() <= 4) {
            editTextLoginUsername.setError(getString(R.string.username_length_incorrect));
            editTextLoginUsername.requestFocus();
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loginProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Snackbar.make(relativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(relativeLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Snackbar.make(relativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


}
