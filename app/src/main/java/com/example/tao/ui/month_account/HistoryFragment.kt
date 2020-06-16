package com.example.tao.ui.month_account

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.MainActivity
import com.example.tao.OrderApi
import com.example.tao.R
import com.example.tao.ui.order_manage.TableListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class HistoryFragment : Fragment() {

    lateinit var rv:RecyclerView
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_month_account, container, false)
        rv=root.findViewById(R.id.month_history)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        CoroutineScope(Job() + Dispatchers.Main).launch {
            val mHandler = Handler(Looper.getMainLooper())
            try {
                val tables = OrderApi.retrofitService.get_order_history_month(
                    MainActivity.Id,
                    MainActivity.store_name,year,month)
                if (tables.isNotEmpty()) {
                    (rv.adapter as TableListAdapter).notifyDataSetChanged()
                }
                mHandler.postDelayed(kotlinx.coroutines.Runnable {

                }, 0)
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
        return root
    }
}
