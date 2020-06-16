package com.example.tao.ui.order_manage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.R
import kotlinx.android.synthetic.main.table_detail_item.view.*
import kotlinx.android.synthetic.main.table_detail_order_number.view.*

class TableDetailDetailAdapter(val context: Context, val items:ArrayList<menudata>):RecyclerView.Adapter<TableDetailDetailAdapter.ViewHolder>() {
    lateinit var V:View
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        V = LayoutInflater.from(parent.context).inflate(R.layout.table_detail_item, parent, false)
        return ViewHolder(V)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val v=holder.itemView
        val item=items.get(pos)
        v.table_detail_menu.text=item.menu
        v.table_detail_size.text=item.size
        v.table_detail_add.text=item.add
        v.table_detail_count.text=item.count
        v.table_detail_price.text=item.price
        if(pos==items.size-1){
            v.detail_bottom_line.visibility=View.GONE
        }
    }
}