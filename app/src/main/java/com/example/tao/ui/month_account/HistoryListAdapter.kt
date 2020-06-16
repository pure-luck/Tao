package com.example.tao.ui.month_account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.R
import com.example.tao.ui.account.historydata

class HistoryListAdapter(val context: Context, val items:ArrayList<historydata>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var V: View
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        V = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(V)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val v=holder.itemView
        val item=items.get(pos)
    }

}