package com.example.jeonsohh.paige_animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_viewpager_item.*


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mprogressbarAsync : progressAsyncTask
        lateinit var mprogressbarTimer : progressbarTimer
        lateinit var mprogressbarThread : progressbarThread
    }

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
                println("viewpager child count : " + m_viewpager.childCount)

                var progressStatus = 0
                mprogressbarAsync = progressAsyncTask(position)
                mprogressbarAsync.execute()

                mprogressbarTimer = progressbarTimer(position,7000,1000)
             //   mprogressbarTimer.start()

                mprogressbarThread = progressbarThread(position)
          //      mprogressbarThread.start()
            }


        })
    }

    override fun onStart() {
        super.onStart()

        mprogressbarAsync = progressAsyncTask(0)
        mprogressbarAsync.execute()
    }

    inner class progressAsyncTask(position : Int) : AsyncTask<Void, Void, Void>(){
        var progressStatus = 0
        var position = position

        override fun doInBackground(vararg p0: Void?): Void? {
            var temp = m_viewpager.getChildAt(position).findViewWithTag("progressbar"+0) as? ProgressBar
            println("temp class : " + temp?.javaClass)

            while (progressStatus <= 100){
                progressStatus++
                runOnUiThread(Runnable {
                    temp?.setProgress(progressStatus)
                })
                Thread.sleep(100)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {

            super.onPostExecute(result)
        }

    }

    inner class progressbarTimer(position:Int, millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        var progressStatus = 0
        var position = position

        override fun onFinish() {
            println("Timer finished")
        }

        override fun onTick(p0: Long) {

            var temp = m_viewpager.getChildAt(position).findViewWithTag("progressbar"+0) as? ProgressBar
            progressStatus++
            temp?.setProgress(progressStatus*100/7)
            Thread.sleep(100)
        }

    }

    inner class progressbarThread(position: Int) : Thread() {
        lateinit var handler : Handler
        var progressStatus = 0
        var position = position

    //    private var mStop : Boolean = false
   //     private var mPause : Boolean = false
        private val lock = java.lang.Object()

        var temp = m_viewpager.getChildAt(position).findViewWithTag("progressbar"+0) as? ProgressBar

        override fun run() {
      /*      while(!mStop){
                synchronized(this){
                    while(mPause){
                        lock.wait()
                    }
                }
        */        Looper.prepare()
                println("Thread run...")
                handler = object : Handler() {
                    override fun handleMessage(msg: Message?) {
                        progressStatus++
                        temp?.setProgress(progressStatus)
                        Thread.sleep(100)
                    }
                }
                Looper.loop()

          //  }
        }

        public fun stopThread() {
           // mStop = true
        }
        public fun pauseThread(){
            synchronized(lock){
                lock.wait()
            }
          //  mPause = true
        }
        public fun resumeThread(){
            synchronized(lock){
            //    mPause = false
                lock.notify()
            }
        }

    }


}
