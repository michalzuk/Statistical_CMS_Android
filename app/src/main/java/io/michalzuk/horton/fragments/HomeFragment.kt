package io.michalzuk.horton.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.michalzuk.horton.R
import io.michalzuk.horton.models.AllCustomers
import io.michalzuk.horton.models.AllProducts
import io.michalzuk.horton.models.Credentials
import io.michalzuk.horton.models.TotalReviews
import io.michalzuk.horton.services.GlobalStorage
import io.michalzuk.horton.services.WooCommerceMethods
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("credentials")
    private val firebaseUser: FirebaseUser = mAuth.currentUser!!
    private val id: String = firebaseUser.uid


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("p0", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val credentials = snapshot.child(id).getValue(Credentials::class.java)
                snapshot.child("Name").toString()
                GlobalStorage.setDomain(credentials?.domain!!)
                GlobalStorage.setUser(credentials.username!!)
                GlobalStorage.setApiKey(credentials.apiKey!!)
                if (GlobalStorage.isAnyMissing()) {
                    val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(GlobalStorage.getDomain())
                            .addConverterFactory(GsonConverterFactory.create())

                    val retrofit = builder.build()
                    val base = GlobalStorage.getUser() + " " + GlobalStorage.getApiKey()
                    val authHeader: String = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)
                    getTotalCustomers(retrofit, authHeader)
                    getAverageProductPrice(retrofit, authHeader)
                    getPercentOfCustomersWithOrders(retrofit, authHeader)
                    getReviewsRating(retrofit, authHeader)
                }
            }
        })
    }

    private fun getAverageProductPrice(mRetrofit: Retrofit, mAuthHeader: String) {
        val methodCaller = mRetrofit.create(WooCommerceMethods::class.java)
        val callAllProductsNames = methodCaller
                .getProductsAmount("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callAllProductsNames.enqueue(object : Callback<List<AllProducts>> {
            override fun onFailure(call: Call<List<AllProducts>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_charts), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<AllProducts>>, response: Response<List<AllProducts>>) {
                val totalOrdersList: List<AllProducts> = response.body()!!
                var totalPrice = 0
                for (item in totalOrdersList) {
                    totalPrice += item.price.toInt()
                }
                val averagePrice = totalPrice / totalOrdersList.size
                average_product_price_value.text = averagePrice.toString()

            }

        })
    }

    private fun getTotalCustomers(mRetrofit: Retrofit, mAuthHeader: String) {
        val methodCaller = mRetrofit.create(WooCommerceMethods::class.java)
        val callMethod = methodCaller
                .getTotalCustomers("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callMethod.enqueue(object : Callback<List<AllCustomers>> {
            override fun onFailure(call: Call<List<AllCustomers>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_home), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<AllCustomers>>, response: Response<List<AllCustomers>>) {
                val list: List<AllCustomers> = response.body()!!
                var customersAmount = 0
                for (item in list) {
                    customersAmount += item.total.toInt()
                }
                total_customers_value.text = customersAmount.toString()
            }
        })
    }

    private fun getPercentOfCustomersWithOrders(mRetrofit: Retrofit, mAuthHeader: String) {
        val methodCaller = mRetrofit.create(WooCommerceMethods::class.java)
        val callMethod = methodCaller
                .getTotalCustomers("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callMethod.enqueue(object : Callback<List<AllCustomers>> {
            override fun onFailure(call: Call<List<AllCustomers>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_charts), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<AllCustomers>>, response: Response<List<AllCustomers>>) {
                val list: List<AllCustomers> = response.body()!!
                val percentOfPaying: Float = (((list[0].total.toFloat() +7) /
                        (list[1].total.toFloat() + 13)) * 100)
                val percentOfPayingText = "$percentOfPaying %"
                percent_paying_users_value.text = percentOfPayingText
            }
        })
    }

    private fun getReviewsRating(mRetrofit: Retrofit, mAuthHeader: String) {
        val methodCaller = mRetrofit.create(WooCommerceMethods::class.java)
        val callMethod = methodCaller
                .getTotalReviews("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callMethod.enqueue(object : Callback<List<TotalReviews>> {
            override fun onFailure(call: Call<List<TotalReviews>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_charts), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<TotalReviews>>, response: Response<List<TotalReviews>>) {
                val list: List<TotalReviews> = response.body()!!
                rating_1.text = list[0].total
                rating_2.text = list[1].total
                rating_3.text = list[2].total
                rating_4.text = list[3].total
                rating_5.text = list[4].total

            }
        })
    }
}
