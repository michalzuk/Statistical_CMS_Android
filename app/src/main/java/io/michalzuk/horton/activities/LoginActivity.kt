package io.michalzuk.horton.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider


private const val RC_SIGN_IN = 7

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var loginLayout: RelativeLayout? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        val loginButton = findViewById<View>(R.id.login_button) as Button
        val registerButton = findViewById<View>(R.id.sign_up_text_view) as TextView
        loginLayout = findViewById(R.id.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        google_sign_in.setOnClickListener {
            signIn()
        }



        loginButton.setOnClickListener {
            login()
        }

        registerButton.setOnClickListener {
            register()
        }

    }

    private fun login() {
        login_progressbar.visibility = View.VISIBLE
        val emailTxt = findViewById<View>(R.id.login_email) as EditText
        val passwordTxt = findViewById<View>(R.id.login_password) as EditText

        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        if (email.isEmpty() ) {
            Snackbar.make(activity_login, R.string.field_cannot_be_empty, Snackbar.LENGTH_LONG)
                    .show()
            emailTxt.requestFocus()
        }
        else if (password.isEmpty()) {
            Snackbar.make(activity_login, R.string.field_cannot_be_empty, Snackbar.LENGTH_LONG)
                    .show()
            passwordTxt.requestFocus()
        } else {
            deactivateScreen(getString(R.string.will_be_logged_in))
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Snackbar.make(activity_login, R.string.field_cannot_be_empty, Snackbar.LENGTH_LONG).show()
                        }
            }
        }
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }

        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun deactivateScreen(snackBarString: String) {
        login_button.setBackgroundColor(R.color.lightGrey)
        login_progressbar!!.visibility = View.GONE
        Snackbar.make(loginLayout!!, snackBarString, Snackbar.LENGTH_SHORT).show()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login", "signInWithCredential:success")
                        val user = mAuth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login", "signInWithCredential:failure", task.exception)
                        Snackbar.make(activity_login, R.string.something_went_wrong, Snackbar.LENGTH_LONG).show()
                    }

                    // ...
                }
    }
}