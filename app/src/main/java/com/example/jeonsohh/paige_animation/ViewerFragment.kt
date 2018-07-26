package com.example.jeonsohh.paige_animation


import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_viewpager_item.*
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*


class ViewerFragment : Fragment(){
    var currentSubItem : Int = 0
    lateinit var animatorSet : AnimatorSet
    lateinit var items : MainActivity.bigItem

    companion object {
        val TAG = "ViewerFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "=== onCreateView ===")
        return inflater.inflate(R.layout.activity_viewpager_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "=== onViewCreated ===")

        items = arguments!!.getSerializable("items") as MainActivity.bigItem
        Log.e(TAG, items.main_text)

        imageview_viewpager.setImageResource(items.main_image) //head이미지
        textview_viewpager.setText(items.main_text) //기사 head
        m_textview_on_slidingdrawer.setText(items.main_text) //기사 내용. 수정

        /* ProgressBar 만들기 */
        val subitemSize = items.subitem.size
        for( i in 0..(subitemSize)) {
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                setTag("progressbar"+i)
            }
            val progressbarParams = LinearLayout.LayoutParams(progresslayout_viewpager!!.measuredWidth / subitemSize+1, ViewGroup.LayoutParams.MATCH_PARENT). apply {
                setMargins(10,0,10,0)
                weight = 1F
            }
            progresslayout_viewpager!!.addView(progressBar,i, progressbarParams)
        }
        println("child? " + view.progresslayout_viewpager.childCount)

        /* longClick 애니메이션 정지*/
        imageview_viewpager.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                println("long click")
                animatorSet.pause()
                view.m_pausedlayout.visibility = View.VISIBLE
                return true
            }
        })

        /* onTouch 애니메이션 재시작 */
        imageview_viewpager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, m: MotionEvent?): Boolean {

                when (m!!.action) {
                    MotionEvent.ACTION_DOWN -> { //초기 위치와 시간 기억

                    }
                    MotionEvent.ACTION_UP -> { // 마우스 up
                        println("Action UP!!! ")
                        animatorSet.resume()
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

    }

    fun animationStart() {

        animatorSet = setAnimation(currentSubItem)

        animatorSet.addListener(object : Animator.AnimatorListener{

            var childCount = progresslayout_viewpager.childCount
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                Log.d( TAG, " === Start Animation ===  " )
                if (currentSubItem in 1 .. childCount) {
                    imageview_viewpager.setImageResource(items.subitem[currentSubItem - 1].main_image)
                    m_textview_on_slidingdrawer.setText(items.subitem[currentSubItem - 1].main_text) //기사 내용. 수정
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                Log.d( TAG, " === End Animation ===  " )

           //     Log.d( TAG, "current sub item num : " + currentSubItem)
           //     Log.d( TAG,"-----Child : " +progresslayout_viewpager.javaClass)
           //     Log.d( TAG,"childCount : " + childCount)
                if (currentSubItem < childCount-1){
                    currentSubItem++
                    animationStart()

               }else{
                    currentSubItem = 0
                }


            }

        })
        animatorSet.start()

    }

    fun setAnimation(currentSub : Int) : AnimatorSet {
        var mImageview = imageview_viewpager
        var mProgressbar = progresslayout_viewpager!!.findViewWithTag<ProgressBar>("progressbar"+currentSub) as ProgressBar

        var anim_progressbar = ObjectAnimator.ofInt(mProgressbar, "progress", 0, 100).apply {
            setDuration(7000)
            setInterpolator(LinearInterpolator())
        } //progressbar animation

        var anim_zoomin = AnimatorInflater.loadAnimator(mImageview.context, R.animator.zoom_in) as AnimatorSet  //property animation방식
        anim_zoomin.setTarget(mImageview)

        anim_zoomin.playTogether(anim_progressbar)

        return anim_zoomin
    }

    fun clearAnimation(){
        Log.d( TAG, " === Clear Animation ===  " )

        currentSubItem = 0
        imageview_viewpager.animate().cancel()
        for(i in 0 .. items.subitem.size) {
            (progresslayout_viewpager!!.findViewWithTag<ProgressBar>("progressbar" + i) as ProgressBar).animate().cancel()
        }
    }
}