package com.example.tao

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    lateinit var imm:InputMethodManager
    lateinit var id:EditText
    lateinit var password:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login_button=findViewById<Button>(R.id.login)
        id = findViewById<EditText>(R.id.username)
        password=findViewById<EditText>(R.id.password)
        val loading : ProgressBar = findViewById(R.id.loading)
        imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v=  findViewById<ConstraintLayout>(R.id.container1)
        v.setOnClickListener({
            hideKeyboard()
        })
        login_button.setOnClickListener({
            hideKeyboard()
            val ID=id.text.toString()
            val PW=password.text.toString()
            CoroutineScope(newSingleThreadContext("loginthread")).launch {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    loading.visibility=View.VISIBLE
                    password.text.clear()
                },0)
                val msg:Message=mHandler.obtainMessage()
                mHandler.sendMessage(msg)
                val json = JSONObject()
                json.put("id",ID)
                json.put("password",PW)
                val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
                val Auth = OrderApi.retrofitService.login(body).execute()
                if (Auth.isSuccessful) {
                    val intent = Intent(this@LoginActivity, StoreSelectActivity::class.java)
                    intent.putExtra("id", ID)
                    intent.putExtra("pw", PW)
                    intent.putExtra("auth",Auth.body()?.access_token)
                    mHandler.postDelayed({
                        loading.visibility = View.GONE
                    },0)
                    AUTH= Auth.body()?.access_token!!
                    startActivity(intent)
                    finish()
                }
                else{
                    mHandler.postDelayed(Runnable(){
                        Toast.makeText(this@LoginActivity,"회원정보가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                    },0)
                }
                mHandler.postDelayed({
                    loading.visibility = View.GONE
                },0)
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    fun hideKeyboard() {
            imm.hideSoftInputFromWindow(id.getWindowToken(), 0)
            imm.hideSoftInputFromWindow(password.getWindowToken(), 0)
    }
    companion object{
        @JvmStatic
        lateinit var AUTH:String
    }
}