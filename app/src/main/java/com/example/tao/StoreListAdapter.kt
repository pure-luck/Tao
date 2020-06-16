package com.example.tao

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.store_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class StoreListAdapter(val context: Context, val items:ArrayList<store>):RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.store_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val v= holder.itemView
        val item=items.get(pos)
        v.store_address.text = item.address
        v.store_name.text=item.name
        Glide
            .with(v.context)
            .load(item.img_url)
            .centerCrop()
            .placeholder(android.R.drawable.stat_sys_upload)
            .into(v.store_img)
        v.setOnClickListener({
            val intent = Intent(context,MainActivity::class.java)
            sock_on(item.id,item.name)
            intent.putExtra("id",item.id)
            intent.putExtra("store_name",item.name)
            ContextCompat.startActivity(context,intent,null)
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    fun sock_on(id:String, store_name:String){
        CoroutineScope(Job()+ Dispatchers.IO).launch {
            Log.d("Connecting......","Socketio")
            socket_io.connect()
            val json=JSONObject()
            json.put("id",id)
            json.put("store_name",store_name)
            json.put("manager",true)
            socket_io.emit("identify",json)
        }
    }
    companion object{
        @JvmStatic
        val ip = "http://52.79.236.212:5000"
        val socket_io=IO.socket(ip)
    }
}