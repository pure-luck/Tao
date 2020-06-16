package com.example.tao.ui.order_manage

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.MainActivity.Companion.Id
import com.example.tao.MainActivity.Companion.store_name
import com.example.tao.OrderApi
import com.example.tao.R
import com.example.tao.StoreListAdapter.Companion.socket_io
import com.example.tao.order
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.lang.Runnable

class OrderManageFragment : Fragment() {
    lateinit var rv:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_order_manage, container, false)
        rv=root.findViewById<RecyclerView>(R.id.table_list)
        RV=rv
        val tablelist:ArrayList<order> = arrayListOf()
        rv.adapter=TableListAdapter(this.requireContext(),tablelist)
        val configuration:Configuration = resources.configuration
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.layoutManager = GridLayoutManager(context, 2)
        }
        else{
            rv.layoutManager = GridLayoutManager(context, 4)
        }
        CoroutineScope(Job()+ Dispatchers.Main).launch {
            val mHandler = Handler(Looper.getMainLooper())
            try {
                val tables = OrderApi.retrofitService.get_tables(Id, store_name)
                if (tables.isNotEmpty()) {
                    tablelist.addAll(tables)
                    (rv.adapter as TableListAdapter).notifyDataSetChanged()
                }
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
        socket_io.on("receive-order",{
            for(i in 0..tablelist.size-1){
                val a = JSONObject(it[0].toString())
                if(tablelist.get(i).table_number==a.getString("table_number")){
                    tablelist.get(i).new = true
                    val mHandler = Handler(Looper.getMainLooper())
                    mHandler.postDelayed(Runnable{
                        rv.adapter?.notifyDataSetChanged()
                    },0)
                }
            }
        })
        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.layoutManager = GridLayoutManager(context, 2)
        }
        else{
            rv.layoutManager = GridLayoutManager(context, 4)
        }
    }


    companion object{
        @JvmStatic
        lateinit var RV:RecyclerView
    }
}