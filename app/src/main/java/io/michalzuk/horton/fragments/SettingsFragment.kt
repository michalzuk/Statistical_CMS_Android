package io.michalzuk.horton.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.R.attr.key
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.michalzuk.horton.R
import io.michalzuk.horton.activities.LoginActivity

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        val signOut = preferenceScreen.findPreference("pref_key_sign_out")


    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference) {
            preferenceManager.findPreference("pref_key_sign_out") -> {
                startActivity(Intent(this.context, LoginActivity::class.java))
            }
            else -> Log.e("XXX", "NULL")
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

        when (key) {

        }
    }


}
