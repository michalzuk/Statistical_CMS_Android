package io.michalzuk.horton.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.activity_sing_up.*

class RegisterActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val createNewAccountButton = findViewById<View>(R.id.create_new_account) as Button

        mDatabase = FirebaseDatabase.getInstance().getReference("Names")


        createNewAccountButton.setOnClickListener(View.OnClickListener { view ->
            register()
        })
    }

    private fun register() {
        val emailText = findViewById<View>(R.id.login_mail) as EditText
        val passwordText = findViewById<View>(R.id.login_password) as EditText
        val usernameText = findViewById<View>(R.id.login_username) as EditText

        var email = emailText.text.toString()
        var password = passwordText.text.toString()
        var username = usernameText.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Snackbar.make(activity_sign_up, "XXX", Snackbar.LENGTH_LONG).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            val uid = user!!.uid.toString()
                            mDatabase.child(uid).child("Name").setValue(username)
                            Snackbar.make(activity_sign_up, "XXX", Snackbar.LENGTH_LONG).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Snackbar.make(activity_sign_up, "XXX", Snackbar.LENGTH_LONG).show()
                        }
                    })
        }
    }
}