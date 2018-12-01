package io.michalzuk.horton.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.activity_sing_up.*


class RegisterActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mDatabase: DatabaseReference
    private var registerLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val createNewAccountButton = findViewById<View>(R.id.create_new_account) as Button

        mDatabase = FirebaseDatabase.getInstance().getReference("Names")
        registerLayout = findViewById(R.id.activity_sign_up)
        sign_up_progressbar.visibility = View.INVISIBLE

        createNewAccountButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = login_mail.text.toString().trim()
        val password = login_password.text.toString().trim()

        validateRegistration(email, password)
        sign_up_progressbar.visibility = View.VISIBLE

    }

    private fun validateRegistration(email: String, password: String) {
        when {
            email.isEmpty() -> {
                login_mail.error = "Email is required"
                login_mail.requestFocus()
            }
            password.isEmpty() -> {
                login_password.error = "Password is required"
                login_password.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                login_password.error = "Please enter a valid email"
                login_mail.requestFocus()
            }
            password.length < 8 -> {
                login_password.error = "Minimum password length is equal to 6"
                login_password.requestFocus()
            }
            else -> this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
                when {
                    task.isSuccessful -> {
                        finish()
                        deactivateScreen(getString(R.string.successfully_registred))
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    task.isCanceled -> deactivateScreen(getString(R.string.action_canceled))
                    task.exception is FirebaseAuthUserCollisionException -> deactivateScreen(getString(R.string.exception_thrown))
                    else -> deactivateScreen(getString(R.string.something_went_wrong))
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun deactivateScreen(snackBarString : String) {
        create_new_account.setBackgroundColor(R.color.lightGrey)
        sign_up_progressbar.visibility = View.GONE
        Snackbar.make(registerLayout!!, snackBarString, Snackbar.LENGTH_SHORT).show()
    }
}