package io.michalzuk.horton.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<View>(R.id.login_button) as Button
        val registerButton = findViewById<View>(R.id.sign_up_text_view) as TextView

        loginButton.setOnClickListener {
            login()
        }

        registerButton.setOnClickListener {
            register()
        }
    }


    private fun login() {
        login_progressbar.visibility = View.VISIBLE
        val emailTxt = findViewById<View>(R.id.login_mail) as EditText
        val passwordTxt = findViewById<View>(R.id.login_password) as EditText

        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_LONG).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            login_progressbar.visibility = View.GONE
                            startActivity(Intent(this, MainActivity::class.java))
                            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_LONG).show()
                        }
                    }
        }
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}