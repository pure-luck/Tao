package com.example.tao.ui.order_manage

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.MainActivity.Companion.Id
import com.example.tao.MainActivity.Companion.store_name
import com.example.tao.OrderApi
import com.example.tao.R
import com.example.tao.StoreListAdapter.Companion.socket_io
import com.example.tao.ui.order_manage.TableDetailActivity.Companion.delete_progressbar
import kotlinx.android.synthetic.main.table_detail_order_number.view.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Runnable

class TableDetailListAdapter(val context: Context, val items:ArrayList<orderdata>,val table_number:String): RecyclerView.Adapter<TableDetailListAdapter.ViewHolder>() {
    lateinit var V:View
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TableDetailListAdapter.ViewHolder {
        V = LayoutInflater.from(parent.context).inflate(R.layout.table_detail_order_number, parent, false)
        return ViewHolder(V)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TableDetailListAdapter.ViewHolder, pos: Int) {
        val v=holder.itemView
        val item=items.get(pos)
        if(pos==0){v.table_detail_call.visibility=View.INVISIBLE
        v.cancel_order.visibility=View.INVISIBLE}
        else{
            v.table_detail_call.setOnClickListener({
                val json=JSONObject()
                json.put("table_number",table_number)
                json.put("id",Id)
                json.put("store_name",store_name)
                json.put("i",item.ordernum.toInt()-1)
                CoroutineScope(Job()+ newSingleThreadContext("delete-order")).launch {
                    val mHandler = Handler(Looper.getMainLooper())
                    mHandler.postDelayed(Runnable {
                        delete_progressbar.visibility = View.VISIBLE
                    },0)
                    val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
                    val response=OrderApi.retrofitService.del_order(body).execute()
                    if(response.isSuccessful){
                        socket_io.emit("order-completed", json)
                        items.removeAt(pos)
                        mHandler.postDelayed(Runnable{
                            delete_progressbar.visibility =View.GONE
                            notifyDataSetChanged()
                        },0)
                    }
                    else{
                        mHandler.postDelayed(Runnable{
                            delete_progressbar.visibility =View.GONE
                        },0)
                        Toast.makeText(context,"오류가 발생했습니다. 다시 시도해 주세요.",Toast.LENGTH_SHORT).show()
                    }
                }
            })
            v.cancel_order.setOnClickListener({
                val json=JSONObject()
                json.put("table_number",table_number)
                json.put("id",Id)
                json.put("store_name",store_name)
                json.put("i",item.ordernum.toInt()-1)
                CoroutineScope(Job()+ newSingleThreadContext("cancel-order")).launch {
                    val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
                    val response=OrderApi.retrofitService.cancel_order(body).execute()
                    if(response.isSuccessful){
                        socket_io.emit("order-completed", json)
                        items.removeAt(pos)
                        val mHandler = Handler(Looper.getMainLooper())
                        mHandler.postDelayed(Runnable{
                            notifyDataSetChanged()
                        },0)
                    }
                    else{
                        Toast.makeText(context,"오류가 발생했습니다. 다시 시도해 주세요.",Toast.LENGTH_SHORT).show()
                    }
                }
            })
            var total=0
            for(i in 0..item.menus.size-1){
                total += item.menus.get(i).price.toInt() * item.menus.get(i).count.toInt()
            }
            v.table_detail_total.text = total.toString()
        }

        v.table_detail_number.text = item.ordernum
        v.table_detail_time.text = item.time
        v.table_order_list.layoutManager = LinearLayoutManager(context)
        v.table_order_list.adapter = TableDetailDetailAdapter(context,item.menus)
    }
}