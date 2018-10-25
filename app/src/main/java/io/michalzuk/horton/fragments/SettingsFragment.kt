package io.michalzuk.horton.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.michalzuk.horton.R
import io.michalzuk.horton.activities.LoginActivity
import io.michalzuk.horton.models.Credentials
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("credentials")
    private val firebaseUser: FirebaseUser = mAuth.currentUser!!
    private val id: String = firebaseUser.uid


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sign_out_button.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        save_credentials_button.setOnClickListener { view ->
            saveCredentials()
            hideSoftKeyboard(this.activity!!, view)
        }
    }

    override fun onStart() {
        super.onStart()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Snackbar.make(view!!.findViewById(R.id.fragment_settings), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val credentials = dataSnapshot.child(id).getValue(Credentials::class.java)
                if (dataSnapshot.child(id).hasChildren())
                    Log.i("XDDDD: ", "XDDDD")
                credentials_api_key.setText(credentials?.apiKey)
                credentials_username.setText(credentials?.username)
                credentials_domain.setText(credentials?.domain)
            }

        })
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

    private fun hideSoftKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
}

