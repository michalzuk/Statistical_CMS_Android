package io.michalzuk.horton.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Base64
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
import io.michalzuk.horton.models.SystemStatus
import io.michalzuk.horton.services.GlobalStorage
import io.michalzuk.horton.services.WooCommerceRequests
import kotlinx.android.synthetic.main.fragment_settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


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

        save_credentials_button.setOnClickListener {
            saveCredentials()
            hideSoftKeyboard(this.activity!!, it)
        }
    }

    override fun onStart() {
        super.onStart()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("p0", "loadPost:onCancelled", p0.toException())
                Snackbar.make(view!!.findViewById(R.id.fragment_settings), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val credentials = dataSnapshot.child(id).getValue(Credentials::class.java)
                credentials_api_key.setText(credentials?.apiKey)
                credentials_username.setText(credentials?.username)
                credentials_domain.setText(credentials?.domain)
                GlobalStorage.setApiKey(credentials?.apiKey!!)
                GlobalStorage.setUser(credentials.username!!)
                GlobalStorage.setDomain(credentials.domain!!)
                if (GlobalStorage.isAnyMissing()) {
                    val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(GlobalStorage.getDomain())
                            .addConverterFactory(GsonConverterFactory.create())

                    val retrofit = builder.build()
                    val base = GlobalStorage.getUser() + " " + GlobalStorage.getApiKey()
                    val authHeader: String = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)
                    println("EKS DI MORDO")
                    getServerData(retrofit, authHeader)
                }
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

    private fun getServerData(mRetrofit: Retrofit, mAuthHeader: String) {
        val methodCaller = mRetrofit.create(WooCommerceRequests::class.java)
        val callAllProductsNames = methodCaller
                .getServerData("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callAllProductsNames.enqueue(object : Callback<SystemStatus> {
            override fun onFailure(call: Call<SystemStatus>, t: Throwable) {
                println("SSS " + call)
                println("SSSS " + t)
                Snackbar.make(view!!.findViewById(R.id.fragment_settings), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SystemStatus>, response: Response<SystemStatus>) {
                val responseList: SystemStatus = response.body()!!
                println("EKS DI " + Objects.toString(responseList))

            }

        })
    }
}

