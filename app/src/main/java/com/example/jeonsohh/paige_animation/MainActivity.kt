package com.example.jeonsohh.paige_animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_viewpager_item.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_viewpager.adapter = myViewPagerAdapter()

    }

    class myViewPagerAdapter() : PagerAdapter() {
        // var anim: Animation? = null
        lateinit var anim_zoomin : AnimatorSet

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

            anim_zoomin = AnimatorInflater.loadAnimator(container.context, R.animator.zoom_in) as AnimatorSet  //property animation방식
            anim_zoomin.setTarget(view.m_viewpager_imageview)
            anim_zoomin.start()

            view.m_viewpager_imageview.setOnTouchListener { view, motionEvent -> GestureDetector(MyGestureListener()).onTouchEvent(motionEvent) }

          /*  view.m_viewpager_imageview.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, m: MotionEvent?): Boolean {
                    var cordx: Float? = null
                    var cordy: Float? = null
                    val LONGPRESS_TIMEOUT  = ViewConfiguration.getLongPressTimeout()
                    val TAP_TIMEOUT = ViewConfiguration.getTapTimeout()
                    // Long Press 판단하는 time 가져옴
                    var touchslop: Int = ViewConfiguration.get(v!!.context).scaledTouchSlop //drag범위 인식하는 기본값
                    val longClickHandler = object : Handler() {
                        override fun handleMessage(msg: Message?) {
                           // println(" anim pause??????")
                            anim_zoomin.pause()
                        }
                    }

                    when (m!!.action) {
                        MotionEvent.ACTION_DOWN -> { //초기 위치와 시간 기억
                            cordx = m.x
                            cordy = m.y
                            println("Action down....")
                            longClickHandler.sendEmptyMessageDelayed(0, LONGPRESS_TIMEOUT.toLong())
                            return true
                        }

                        MotionEvent.ACTION_UP -> { // 마우스 up
                            println("Action UP!!! ")
                            anim_zoomin.resume()
                            longClickHandler.removeMessages(0)
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> { //일정범위
                            println("Action Move!!! ")


                        }
                        else -> {

                        }
                    }

                    return false
                }
            })*/

            container.addView(view)
            return view
        }

        class MyGestureListener() : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_MIN_DISTANCE = 150
            private val SWIPE_MAX_OFF_PATH = 100
            private val SWIPE_THRESHOLD_VELOCITY = 100

            override fun onDown(e: MotionEvent?): Boolean {
                return super.onDown(e)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                if( e1 == null || e2 == null)   return false
                val cordX = e2.x - e1.x
                val cordY = e1.y - e2.y
                println("swipe up")

                if(Math.abs(cordY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(cordX) >= SWIPE_MIN_DISTANCE){
                    if(cordX > 0){
                        //swipe right
                    }else{
                        //swipe left
                    }
                }else if(Math.abs(cordX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(cordY) >= SWIPE_MIN_DISTANCE){
                    if(cordY > 0){
                        println("swipe up")
                        //swipe up
                    }else{
                        //swipe down
                        println("swipe down")
                    }
                }

                return false

            }

            override fun onLongPress(e: MotionEvent?) {
                println("longpress")
                super.onLongPress(e)
            }
        }


    }

    data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>)
    data class smallItem(var main_image: Int, var main_text: String)

}
