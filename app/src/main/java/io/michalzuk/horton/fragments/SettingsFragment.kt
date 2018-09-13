package io.michalzuk.horton.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import io.michalzuk.horton.activities.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sign_out_button.setOnClickListener { view ->
            mAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_SHORT).show()
        }
    }
}

