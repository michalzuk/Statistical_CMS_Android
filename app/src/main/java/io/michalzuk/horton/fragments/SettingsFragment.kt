package io.michalzuk.horton.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import io.michalzuk.horton.activities.LoginActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import io.michalzuk.horton.activities.MainActivity
import kotlinx.android.synthetic.main.activity_login.*


class SettingsFragment : Fragment() {

    val mAuth = FirebaseAuth.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sign_out_button.setOnClickListener(View.OnClickListener { view ->
            mAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            Snackbar.make(activity_login, "XXX", Snackbar.LENGTH_SHORT).show()
        })
    }
}

