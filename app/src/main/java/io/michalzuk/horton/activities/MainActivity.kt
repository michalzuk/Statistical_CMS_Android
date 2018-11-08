package io.michalzuk.horton.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.michalzuk.horton.fragments.*
import io.michalzuk.horton.R
import io.michalzuk.horton.models.Credentials
import io.michalzuk.horton.services.GlobalStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser = mAuth.currentUser!!
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("credentials")
    private val id: String = firebaseUser.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.w("p0", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val credentials = snapshot.child(id).getValue(Credentials::class.java)
                snapshot.child("Name").toString()
                GlobalStorage.setDomain(credentials?.domain!!)
                GlobalStorage.setUser(credentials.username!!)
                GlobalStorage.setApiKey(credentials.apiKey!!)
            }
        })


        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> HomeFragment()
                1 -> FeedFragment()
                2 -> ChartsFragment()
                3 -> ContactUsFragment()
                4 -> SettingsFragment()
                else -> HomeFragment()
            }
        }

        override fun getCount(): Int {
            return 5
        }

    }
}
