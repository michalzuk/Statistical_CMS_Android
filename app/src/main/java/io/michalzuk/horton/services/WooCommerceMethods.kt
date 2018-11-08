package io.michalzuk.horton.services

import io.michalzuk.horton.models.TopSellers
import io.michalzuk.horton.models.TotalOrders
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WooCommerceMethods {

    @GET("/wp-json/wc/v3/reports/orders/totals/")
    fun getTotalOrders (@Header("Authorization")  authHeader : String)
            : Call<List<TotalOrders>>

    @GET("https://so460.sohost.pl/wp-json/wc/v3/reports/top_sellers?period=year")
    fun getTopSellers (@Header("Authorization") authHeader: String) : Call<List<TopSellers>>
}