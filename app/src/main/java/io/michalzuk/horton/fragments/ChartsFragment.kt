package io.michalzuk.horton.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.michalzuk.horton.R
import io.michalzuk.horton.models.TopSellers
import io.michalzuk.horton.models.TotalOrders
import io.michalzuk.horton.services.CredentialsToBase64
import io.michalzuk.horton.services.GlobalStorage
import io.michalzuk.horton.services.WooCommerceMethods
import kotlinx.android.synthetic.main.fragment_charts.*
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChartsFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser = mAuth.currentUser!!
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("credentials")
    private val id: String = firebaseUser.uid
    private val authHeader : String = CredentialsToBase64(GlobalStorage.getUser(),
            GlobalStorage.getApiKey()).authorizationHeader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(GlobalStorage.getDomain())
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val base = GlobalStorage.getUser() + " " + GlobalStorage.getApiKey()
        val authHeader : String = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)

        val methodCaller = retrofit.create(WooCommerceMethods::class.java)
        val callTotalOrders = methodCaller
                .getTotalOrders("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")

        callTotalOrders.enqueue(object : Callback<List<TotalOrders>> {
            override fun onFailure(call: Call<List<TotalOrders>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_charts), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<TotalOrders>>, response: Response<List<TotalOrders>>) {
                val totalOrdersList : List<TotalOrders> = response.body()!!
                generatePieChart(totalOrdersList)
            }

        })

        val callTopSellers = methodCaller
                .getTopSellers("Basic Y2tfZjI4MjUzOTBiZjI5NTkwNWZjYmY1Njk5ODhkYzc5NzgwYjIyZjg3Zjpjc19lMGM0ZjU1YWVkNzNkNGVlMjFiNGRiYjgzZTk5MmYwN2MwMDU1ZDE0")


        callTopSellers.enqueue(object : Callback<List<TopSellers>> {
            override fun onFailure(call: Call<List<TopSellers>>, t: Throwable) {
                Snackbar.make(view!!.findViewById(R.id.fragment_charts), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<TopSellers>>, response: Response<List<TopSellers>>) {
                val topSellers : List<TopSellers> = response.body()!!
                generateBarChart(topSellers)
            }
        })

        tv1.text = GlobalStorage.getApiKey()
        tv2.text = GlobalStorage.getDomain()
        tv3.text = GlobalStorage.getUser()
    }

    private fun generateBarChart(barItem : List<TopSellers>) {
        barchart.addBar(BarModel(barItem[0].name,barItem[0].quantity.toFloat(), -0xedcbaa))
        barchart.addBar(BarModel(barItem[1].name,barItem[1].quantity.toFloat(), -0xa9cbaa))
        barchart.addBar(BarModel(barItem[2].name,barItem[2].quantity.toFloat(), -0x78c0aa))
        barchart.addBar(BarModel(barItem[3].name,barItem[3].quantity.toFloat(), -0xa9480f))
        barchart.addBar(BarModel(barItem[4].name,barItem[4].quantity.toFloat(), -0xe00b54))
        barchart.addBar(BarModel(barItem[5].name,barItem[5].quantity.toFloat(), -0xe45b1a))

        barchart.startAnimation()
    }

    private fun generatePieChart(to : List<TotalOrders>) {
        piechart.addPieSlice(PieModel(to[0].name, to[0].total.toFloat() + 30f, Color.parseColor("#A90D05")))
        piechart.addPieSlice(PieModel(to[1].name, to[1].total.toFloat() + 18f, Color.parseColor("#FE6DA8")))
        piechart.addPieSlice(PieModel(to[2].name, to[2].total.toFloat() + 12f, Color.parseColor("#56B7F1")))
        piechart.addPieSlice(PieModel(to[3].name, to[3].total.toFloat() + 45, Color.parseColor("#CDA67F")))
        piechart.addPieSlice(PieModel(to[4].name, to[4].total.toFloat() + 10f, Color.parseColor("#1C6497")))
        piechart.addPieSlice(PieModel(to[5].name, to[5].total.toFloat() + 9f, Color.parseColor("#06A05D")))
        piechart.addPieSlice(PieModel(to[6].name, to[6].total.toFloat() + 17f, Color.parseColor("#FED70E")))
        piechart.startAnimation()
    }

}
