package io.michalzuk.horton.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.michalzuk.horton.R
import io.michalzuk.horton.activities.LoginActivity
import io.michalzuk.horton.models.Credentials
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("credentials");
    private val firebaseUser: FirebaseUser = mAuth.currentUser!!;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sign_out_button.setOnClickListener { view ->
            mAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        save_credentials_button.setOnClickListener { view ->
            saveCredentials();
        }
    }

    private fun saveCredentials() {
        val domain = credentials_domain.text.toString().trim()
        val username: String = credentials_username.text.toString().trim()
        val apiKey: String = credentials_api_key.text.toString().trim()

        if (!TextUtils.isEmpty(domain)) {
            val id: String = firebaseUser.uid
            val credentials = Credentials(domain, username, apiKey)
            databaseReference.child(id).setValue(credentials)
            Snackbar.make(view!!.findViewById(R.id.fragment_settings), getString(R.string.credentials_added), Snackbar.LENGTH_SHORT).show()

        }
    }
}

