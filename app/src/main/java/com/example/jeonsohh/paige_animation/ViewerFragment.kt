package com.example.jeonsohh.paige_animation


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*


class ViewerFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.activity_viewpager_item, container, false)

        var items = arguments!!.getSerializable("items") as MainActivity2.bigItem
        println(items.main_text)

        view.run {
            m_viewpager_imageview.setTag("imageview")
            m_viewpager_imageview.setImageResource(items.main_image) //head이미지
            m_textview_on_imageview.setText(items.main_text) //기사 head
            m_textview_on_slidingdrawer.setText(items.main_text) //기사 내용. 수정
        }

        /* ProgressBar 만들기 */
        val subitemSize = items.subitem.size
        for( i in 0..subitemSize -1) {
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                setTag("progressbar"+i)
            }
            val progressbarParams = LinearLayout.LayoutParams(container!!.measuredWidth / subitemSize, ViewGroup.LayoutParams.MATCH_PARENT). apply {
                setMargins(10,0,10,0)
                weight = 1F
            }
            view.m_progresslayout!!.addView(progressBar,i, progressbarParams)
        }
        println("child? " + view.m_progresslayout.childCount)

        /* longClick 애니메이션 정지*/
        view.m_viewpager_imageview.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                println("long click")
                MainActivity2.animatorSet.pause()
             //   MainActivity2.anim_zoomin.cancel()

                view.m_pausedlayout.visibility = View.VISIBLE
                return true
            }
        })

        /* onTouch 애니메이션 재시작 */
        view.m_viewpager_imageview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, m: MotionEvent?): Boolean {

                when (m!!.action) {
                    MotionEvent.ACTION_DOWN -> { //초기 위치와 시간 기억

                    }
                    MotionEvent.ACTION_UP -> { // 마우스 up
                        println("Action UP!!! ")
                        MainActivity2.animatorSet.resume()
                      //  MainActivity2.anim_zoomin.resume()

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

        return view
    }


}