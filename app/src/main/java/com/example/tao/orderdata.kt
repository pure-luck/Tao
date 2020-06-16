package com.example.tao


import com.google.gson.JsonObject
import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer


data class order(
    val table_number: String,
    val order_lists:String,
    var new:Boolean = false
)

data class order_history(
    val table_number: String,
    val order_lists: String,
    val order_time:Long
)

data class auth(
    val user_id:String,
    val access_token:String
)

data class store(
    val id:String,
    val name:String,
    val address:String,
    val img_url:String
)
