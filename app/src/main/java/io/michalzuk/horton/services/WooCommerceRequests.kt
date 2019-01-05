package io.michalzuk.horton.services

import io.michalzuk.horton.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface zawierWooCommerceRequests {

    @GET("/wp-json/wc/v3/reports/orders/totals/")
    fun getTotalOrders (@Header("Authorization")  authHeader : String)
            : Call<List<TotalOrders>>

    @GET("/wp-json/wc/v3/reports/top_sellers?period=year")
    fun getTopSellers (@Header("Authorization") authHeader: String)
            : Call<List<TopSellers>>

    @GET("/wp-json/wc/v3/products/")
    fun getProductsAmount (@Header("Authorization") authHeader: String)
            : Call<List<AllProducts>>

    @GET("/wp-json/wc/v3/reports/customers/totals")
    fun getTotalCustomers(@Header("Authorization") authHeader: String)
            : Call<List<AllCustomers>>

    @GET("/wp-json/wc/v3/system_status")
    fun getServerData(@Header("Authorization") authHeader: String)
            : Call<SystemStatus>

    @GET("/wp-json/wc/v3/reports/reviews/totals")
    fun getTotalReviews(@Header("Authorization") authHeader: String)
            : Call<List<TotalReviews>>
}