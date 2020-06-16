package com.example.tao.ui.order_manage

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tao.R

import org.json.JSONArray

class TableDetailActivity:AppCompatActivity() {
    lateinit var table_number:String
    lateinit var json: JSONArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_detail)
        val rv = findViewById<RecyclerView>(R.id.table_detail_list)
        table_number=intent.getStringExtra("table_number")!!
        setTitle(table_number+"번 테이블 주문 정보")
        json = JSONArray(intent.getStringExtra("json"))
        delete_progressbar=findViewById<ProgressBar>(R.id.del_progressBar)
        rv.layoutManager= LinearLayoutManager(this)
        val detaillist:ArrayList<orderdata> = arrayListOf()
        val Menulist:ArrayList<menudata> = arrayListOf()
        val adapter = TableDetailListAdapter(this,detaillist,table_number)
        Menulist.add(menudata("메뉴","선택","추가","개수","가격"))
        detaillist.add(orderdata(" ",Menulist,"주문시각"))
        for(i in 0..json.length()-1){
            val menulist:ArrayList<menudata> = arrayListOf()
            var Price:Int=0
            val l = json.getJSONObject(i)
            val A = l.getJSONArray("order")
            var order_str = ""
            for(J in 0..A.length()-1) {
                val j = A.getJSONObject(J)
                val name = j.getString("name")
                val price = j.getString("price")
                val count = j.getString("count")
                Price += price.toInt() * count.toInt()
                val topping = j.getJSONObject("topping")
                val size = topping.getString("size")
                var toppingstr = ""
                val addtional = topping.getJSONArray("additional")
                for (k in 0..addtional.length() - 1) {
                    val tmp = addtional.getJSONObject(k)
                    if(k!=0)toppingstr+='\n'
                    toppingstr += tmp.getString("name")+" "+tmp.getString("count")
                }
                order_str += "${name} ${count}\n"
                menulist.add(menudata(name,size,toppingstr,count,price))
            }
            order_str+="${Price}원"
            val time = l.getString("order_time")
            detaillist.add(orderdata((i+1).toString(),menulist,time))
        }
        rv.adapter=adapter
    }
    override fun onBackPressed() {
        setResult(1)
        finish()
    }

    companion object{
        @JvmStatic
        lateinit var delete_progressbar:ProgressBar
    }
}