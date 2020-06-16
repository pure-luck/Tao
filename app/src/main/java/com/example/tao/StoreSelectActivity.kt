package com.example.tao

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.lang.Runnable

class StoreSelectActivity:AppCompatActivity() {
    lateinit var id:String
    lateinit var pw:String
    lateinit var auth:String
    lateinit var rv:RecyclerView
    lateinit var loading:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_select)
        id = intent.getStringExtra("id")!!
        pw = intent.getStringExtra("pw")!!
        auth = intent.getStringExtra("auth")!!
        rv=findViewById(R.id.store_list)
        rv.layoutManager= LinearLayoutManager(this)
        val storelist:ArrayList<store> = arrayListOf()
        rv.adapter = StoreListAdapter(this,storelist)
        loading=findViewById(R.id.store_progress)
        CoroutineScope(Job()+ newSingleThreadContext("storeload")).launch {
            val mHandler = Handler(Looper.getMainLooper())
            try {
                mHandler.postDelayed(Runnable {
                    loading.visibility = View.VISIBLE
                }, 0)
                val stores = OrderApi.retrofitService.get_stores(id)
                if (!stores.isEmpty()) {
                    stores.forEach {
                        storelist.addAll(stores)
                    }
                    (rv.adapter as StoreListAdapter).notifyDataSetChanged()
                } else {
                    mHandler.postDelayed(Runnable {
                        Toast.makeText(this@StoreSelectActivity, "등록된 가게가 없습니다", Toast.LENGTH_SHORT)
                            .show()
                    }, 0)
                }
                mHandler.postDelayed(Runnable {
                    loading.visibility = View.GONE
                }, 0)
            }catch (e:Exception){
                loading.visibility = View.GONE
                e.printStackTrace()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}