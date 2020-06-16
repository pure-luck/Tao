package com.example.tao.ui.order_manage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.MainActivity.Companion.mainactivity
import com.example.tao.R
import com.example.tao.order
import kotlinx.android.synthetic.main.order_list_item.view.*
import kotlinx.android.synthetic.main.table_detail_order_number.view.*
import org.json.JSONArray

class OrderListAdapter(val context: Context, val items:ArrayList<String>,val table_number:String,val json:JSONArray,val x: ArrayList<order>): RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val v=holder.itemView
        val item=items.get(pos)
        v.order1.text=item
        v.setOnClickListener({
            x.forEach {
                if(it.table_number==table_number){
                    it.new=false
                }
            }
            val intent = Intent(context,TableDetailActivity::class.java)
            intent.putExtra("table_number",table_number)
            intent.putExtra("json",json.toString())
            startActivityForResult(mainactivity,intent,1,null)
        })
    }
}