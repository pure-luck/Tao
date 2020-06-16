package com.example.tao

import android.text.Editable
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

private const val base_url="http://52.79.236.212:5000/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(base_url)
    .build()

interface OrderApiService{
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body body: RequestBody):Call<auth>
    @GET("orders")
    suspend fun get_orders(@Query("id")id:String,
                           @Query("store_name")store_name:String,
                           @Query("table_number")table_number:Int):order

    @GET("/tables")
    suspend fun get_tables(@Query("id")id:String,
                           @Query("store_name")store_name:String):List<order>

    @GET("order-history-with-date")
    suspend fun get_order_history(@Query("id")id:String,
                                  @Query("store_name")store_name:String,
                                  @Query("year")year:Int,
                                  @Query("month")month:Int,
                                  @Query("day")day:Int):List<order_history>

    @GET("order-history-with-date")
    suspend fun get_order_history_month(@Query("id")id:String,
                                  @Query("store_name")store_name:String,
                                  @Query("year")year:Int,
                                  @Query("month")month:Int):List<order_history>

    @GET("stores")
    suspend fun get_stores(@Query("id")id:String):List<store>

    @POST("cancel-order")
    fun cancel_order(@Body body:RequestBody):Call<Void>

    @POST("del-order")
    fun del_order(@Body body:RequestBody):Call<Void>

}

object OrderApi{
    val retrofitService:OrderApiService by lazy{
        retrofit.create(OrderApiService::class.java)
    }
}