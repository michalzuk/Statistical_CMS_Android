package io.michalzuk.horton.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.michalzuk.horton.R
import io.michalzuk.horton.models.TopSellers
import io.michalzuk.horton.models.TotalOrders
import io.michalzuk.horton.services.GlobalStorage
import io.michalzuk.horton.services.RetrofitInstance
import io.michalzuk.horton.services.WooCommerceRequests
import kotlinx.android.synthetic.main.fragment_charts.*
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartsFragment : Fragment() {

    val LOG = "LOG"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val service = RetrofitInstance.getRetrofitInstance()!!.create(WooCommerceRequests::class.java)
        val credentialsEncoded: String = authHeaderToBase64(GlobalStorage.getUser(), GlobalStorage.getApiKey())

        val callTotalOrders = service.getTotalOrders(credentialsEncoded)
        val callTopSellers = service.getTopSellers(credentialsEncoded)

        callTotalOrders.enqueue(object : Callback<List<TotalOrders>> {
            override fun onFailure(call: Call<List<TotalOrders>>, t: Throwable) {
                Snackbar.make(view.findViewById(R.id.fragment_charts), R.string.something_went_wrong,
                        Snackbar.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<TotalOrders>>,
                                    response: Response<List<TotalOrders>>) {
                val totalOrdersList: List<TotalOrders> = response.body()!!
                generatePieChart(totalOrdersList)
            }
        })



        callTopSellers.enqueue(object : Callback<List<TopSellers>> {
            override fun onFailure(call: Call<List<TopSellers>>, t: Throwable) {
                Snackbar.make(view.findViewById(R.id.fragment_charts), R.string.something_went_wrong,
                        Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<TopSellers>>,
                                    response: Response<List<TopSellers>>) {
                if (response.code() == 200) {
                    val topSellers: List<TopSellers> = response.body()!!
                    if (topSellers.isNotEmpty()) generateBarChart(topSellers)
                } else {
                    Snackbar.make(view.findViewById(R.id.fragment_charts), R.string.something_went_wrong,
                            Snackbar.LENGTH_SHORT).show()
                    Log.w(LOG, "Request returned status code: " + response.code())
                }
            }
        })
    }

    private fun generateBarChart(barItem: List<TopSellers>) {
        barchart.addBar(BarModel(barItem[0].name, barItem[0].quantity.toFloat(), -0xedcbaa))
        barchart.addBar(BarModel(barItem[1].name, barItem[1].quantity.toFloat(), -0xa9cbaa))
        barchart.addBar(BarModel(barItem[2].name, barItem[2].quantity.toFloat(), -0x78c0aa))
        barchart.addBar(BarModel(barItem[3].name, barItem[3].quantity.toFloat(), -0xa9480f))
        barchart.addBar(BarModel(barItem[4].name, barItem[4].quantity.toFloat(), -0xe00b54))
        barchart.addBar(BarModel(barItem[5].name, barItem[5].quantity.toFloat(), -0xe45b1a))

        barchart.startAnimation()
    }

    private fun generatePieChart(item: List<TotalOrders>) {
        piechart.addPieSlice(PieModel(item[0].name, item[0].total.toFloat() + 30f,
                Color.parseColor("#A90D05")))
        piechart.addPieSlice(PieModel(item[1].name, item[1].total.toFloat() + 18f,
                Color.parseColor("#FE6DA8")))
        piechart.addPieSlice(PieModel(item[2].name, item[2].total.toFloat() + 12f,
                Color.parseColor("#56B7F1")))
        piechart.addPieSlice(PieModel(item[3].name, item[3].total.toFloat() + 45,
                Color.parseColor("#CDA67F")))
        piechart.addPieSlice(PieModel(item[4].name, item[4].total.toFloat() + 10f,
                Color.parseColor("#1C6497")))
        piechart.addPieSlice(PieModel(item[5].name, item[5].total.toFloat() + 9f,
                Color.parseColor("#06A05D")))
        piechart.addPieSlice(PieModel(item[6].name, item[6].total.toFloat() + 17f,
                Color.parseColor("#FED70E")))
        piechart.startAnimation()
    }

    private fun authHeaderToBase64(login: String, password: String): String {
        val base = "$login:$password"
        return "Basic " + Base64.encodeToString(base.toByteArray(),
                Base64.NO_WRAP)

    }
}
