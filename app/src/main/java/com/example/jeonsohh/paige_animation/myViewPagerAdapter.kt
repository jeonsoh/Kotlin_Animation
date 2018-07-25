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
                setTag("progressbar"+i)
            }
            val progressbarParams = LinearLayout.LayoutParams(container.measuredWidth / subitemSize, ViewGroup.LayoutParams.MATCH_PARENT). apply {
                setMargins(10,0,10,0)
                weight = 1F
            }
            view.m_progresslayout!!.addView(progressBar,i, progressbarParams)
        }
        println("child? " + view.m_progresslayout.childCount)


        view.m_viewpager_imageview.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                println("long click")
                anim_zoomin.pause()

                   MainActivity.mprogressbarAsync.cancel(true)
              //  MainActivity.mprogressbarTimer.cancel()

         //       MainActivity.mprogressbarThread.pauseThread()
         //       print("thread status ??" + MainActivity.mprogressbarThread.isInterrupted)
        //        print("thread status ??" + MainActivity.mprogressbarThread.isAlive)

                view.m_pausedlayout.visibility = View.VISIBLE
                return true
            }
        })

        view.m_viewpager_imageview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, m: MotionEvent?): Boolean {

                when (m!!.action) {
                    MotionEvent.ACTION_DOWN -> { //초기 위치와 시간 기억

                    }
                    MotionEvent.ACTION_UP -> { // 마우스 up
                        println("Action UP!!! ")
                        anim_zoomin.resume()

                //        if(MainActivity.mprogressbarAsync.isCancelled){
                   //         MainActivity.mprogressbarAsync = MainActivity.progressAsyncTask(position)
                            MainActivity.mprogressbarAsync.execute()
                   //     }

                 //       MainActivity.mprogressbarThread.resumeThread()
                  //      print("thread status ??" + MainActivity.mprogressbarThread.isInterrupted)
                   //     print("thread status ??" + MainActivity.mprogressbarThread.isAlive)

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

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}

data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>)
data class smallItem(var main_image: Int, var main_text: String)