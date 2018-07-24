package com.example.jeonsohh.paige_animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.*
import android.support.v4.view.PagerAdapter
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*
import kotlinx.android.synthetic.main.activity_viewpager_item.*


class myViewPagerAdapter() : PagerAdapter() {
    // var anim: Animation? = null
    //lateinit var anim_zoomin : AnimatorSet
    var progressStatus = 0

    //소분류
    var subBubble = listOf<smallItem>(smallItem(R.drawable.image1_bubble1, "image1_bubble1"),
            smallItem(R.drawable.image1_bubble2, "image1_bubble2"),
            smallItem(R.drawable.image1_bubble3, "image1_bubble3"))
    //대분류
    var bubble = bigItem(R.drawable.image1, "image1", subBubble)

    var subBridge = listOf<smallItem>(smallItem(R.drawable.image2_bridge1, "image2_bridge1"),
            smallItem(R.drawable.image2_bridge2, "image2_bridge2"))
    var bridge = bigItem(R.drawable.image2, "image2", subBridge)

    var items = arrayOf(bubble, bridge)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return items.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(container.context).inflate(R.layout.activity_viewpager_item, container, false).apply {
            m_viewpager_imageview.setImageResource(items[position].main_image) //head이미지
            m_textview_on_imageview.setText(items[position].main_text) //기사 head
            m_textview_on_slidingdrawer.setText(items[position].main_text) //기사 내용. 수정
        }

        var anim_zoomin = AnimatorInflater.loadAnimator(container.context, R.animator.zoom_in) as AnimatorSet  //property animation방식
        anim_zoomin.setTarget(view.m_viewpager_imageview)
        anim_zoomin.start()

        val subitemSize = items[position].subitem.size

        for( i in 0..subitemSize -1) {
            val progressBar = ProgressBar(container.context, null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
            }
            val progressbarParams = LinearLayout.LayoutParams(container.measuredWidth / subitemSize, ViewGroup.LayoutParams.MATCH_PARENT). apply {
                setMargins(10,0,10,0)
                weight = 1F
            }
            view.m_progresslayout_imageview!!.addView(progressBar,i, progressbarParams)
        }
        println("child? " + view.m_progresslayout_imageview.childCount)

              /*  val mhandler = progressbarHandler(view,progressStatus)
        Thread{
            run {
                progressStatus++
                mhandler.sendMessage(Message())
            }
        }.start()*/

       val progressbarTimer = object : CountDownTimer(7000,1000){
            override fun onFinish() {
                Toast.makeText(container.context, "finish first progress...", Toast.LENGTH_LONG).show()
            }

            override fun onTick(p0: Long) {
                progressStatus++
                var temp = view.m_progresslayout_imageview.getChildAt(0) as ProgressBar
                temp.setProgress(progressStatus*100/(7000/1000))

            }

        }
        //progressbarTimer.start()

        view.m_viewpager_imageview.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                println("long click")
                anim_zoomin.pause()
                //progressbarAsync.cancel(true)
             //   progressbarTimer.cancel()
                view.m_pausedlayout.visibility = View.VISIBLE
                return true
            }
        })

        view.m_viewpager_imageview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, m: MotionEvent?): Boolean {
                var cordx: Float? = null
                var cordy: Float? = null
                var touchslop: Int = ViewConfiguration.get(v!!.context).scaledTouchSlop //drag범위 인식하는 기본값

                when (m!!.action) {
                    MotionEvent.ACTION_DOWN -> { //초기 위치와 시간 기억

                    }
                    MotionEvent.ACTION_UP -> { // 마우스 up
                        println("Action UP!!! ")
                        anim_zoomin.resume()
                       // if(progressbarAsync.isCancelled()){
                       //     progressbarAsync.execute()

                       // }
                       //   progressbarTimer.start()
                        view.m_pausedlayout.visibility = View.INVISIBLE
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> { //일정범위
                        // println("Action Move!!! ")

                    }
                }
                return false
            }
        })

        container.addView(view)
        return view
    }

  /*  class progressbarHandler(v:View, count : Int) : Handler() {
        val view = v
        var count = count
        override fun handleMessage(msg: Message?) {
            var temp = view.m_progresslayout_imageview.getChildAt(0) as ProgressBar
            temp.setProgress(count)
            Thread.sleep(100)
            super.handleMessage(msg)
        }
    }*/

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

data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>)
data class smallItem(var main_image: Int, var main_text: String)