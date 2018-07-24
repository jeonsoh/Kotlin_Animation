package com.example.jeonsohh.paige_animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_viewpager_item.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()


        m_viewpager.adapter = myViewPagerAdapter()
        m_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                var progressStatus = 0
                var progressbarAsync = progressAsyncTask()
                progressbarAsync.execute()

            }


        })

    }

    inner class progressAsyncTask : AsyncTask<Void, Void, Void>(){
        var progressStatus = 0


        override fun doInBackground(vararg p0: Void?): Void? {

            while (progressStatus <= 100){
                var temp = m_progresslayout_imageview.getChildAt(0) as ProgressBar
                progressStatus++
                temp.setProgress(progressStatus)
                Thread.sleep(100)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }

    }


}
