package io.michalzuk.horton.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<View>(R.id.login_button) as Button
        val registerButton = findViewById<View>(R.id.sign_up_text_view) as TextView

        loginButton.setOnClickListener(View.OnClickListener { view ->
            login()
        })

        registerButton.setOnClickListener(View.OnClickListener { view ->
            register()
        })
    }


    private fun login() {
        val emailTxt = findViewById<View>(R.id.login_mail) as EditText
        val passwordTxt = findViewById<View>(R.id.login_password) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_LONG).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_LONG).show()
                        }
                    })
        }
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))

    }
}