package com.example.tao.ui.order_manage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.R
import com.example.tao.order
import kotlinx.android.synthetic.main.table_list_item.view.*
import kotlinx.android.synthetic.main.table_list_item.view.table_order_list
import org.json.JSONArray

class TableListAdapter(val context: Context, val items:ArrayList<order>): RecyclerView.Adapter<TableListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        return ViewHolder(V)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val v=holder.itemView
        val item=items.get(pos)
        v.table_number.text = item.table_number
        v.table_order_list.layoutManager = LinearLayoutManager(context)
        if(item.new){v.table_card.setBackgroundColor(getColor(context,R.color.gotorder))}
        else{v.table_card.setBackgroundColor(0xFFFFFF)}
        val json = JSONArray(item.order_lists)
        val a:ArrayList<String> = arrayListOf()
        v.setOnClickListener({
            item.new=false
            val intent = Intent(context,TableDetailActivity::class.java)
            intent.putExtra("table_number",item.table_number)
            intent.putExtra("json",item.order_lists)
            startActivity(context,intent,null)
            notifyDataSetChanged()
        })
        for(i in json.length()-1 downTo 0){
            var Price:Int=0
            val l = json.getJSONObject(i)
            val A = l.getJSONArray("order")
            var order_str = ""
            for(J in 0..A.length()-1) {
                val j = A.getJSONObject(J)
                val name = j.getString("name")
                val price = j.getString("price")
                Price += price.toInt()
                val count = j.getString("count")
                order_str += "${name} ${count}\n"
            }
            order_str+="${Price}원"
            val time = l.getString("order_time")
            a.add(order_str)
        }
        v.table_order_list.adapter = OrderListAdapter(context,a,item.table_number,json,items)
    }
}