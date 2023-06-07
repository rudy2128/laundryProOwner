package com.anthony.laundrypro.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.MyPref
import com.anthony.laundrypro.ui.MainActivity
import com.anthony.laundrypro.ui.login.LoginActivity
import com.anthony.laundrypro.ui.outlet.OutletActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.simpleProgressBar)

        progressBar.visibility = View.VISIBLE

        val thread = Thread{
            try {

                Thread.sleep(3000)
            }catch (e:InterruptedException){
                e.printStackTrace()
            }finally {
                val myPref = MyPref(applicationContext)
                val phone = myPref.phone
                if (phone.isEmpty()){
                    startActivity(Intent(this@SplashActivity, FirstActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()

                }
            }
        }
        thread.start()
    }

}