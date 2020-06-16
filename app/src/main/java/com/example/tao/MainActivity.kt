package com.example.tao

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tao.StoreListAdapter.Companion.socket_io
import com.example.tao.ui.order_manage.OrderManageFragment.Companion.RV
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {

    var lastTimeBackPressed=System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        Id=intent.getStringExtra("id")!!
        store_name=intent.getStringExtra("store_name")!!
        mainactivity=this
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_order_manage, R.id.navigation_account, R.id.navigation_month_account))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
            socket_io.disconnect()
            super.onBackPressed()
        }
        else {
            lastTimeBackPressed = System.currentTimeMillis()
            Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            RV.adapter?.notifyDataSetChanged()
        }
    }

    companion object{
        @JvmStatic
        var Id=""
        var store_name=""
        lateinit var mainactivity:MainActivity
    }
}