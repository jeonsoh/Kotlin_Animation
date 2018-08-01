package com.example.jeonsohh.paige_animation


import android.animation.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_viewpager_item.*
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*
import org.greenrobot.eventbus.EventBus
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.design.widget.BottomSheetBehavior
import android.view.animation.*


class ViewerFragment : Fragment() {
    var mCurrentProgressbar: Int = 0
    var mAnimatorSet: AnimatorSet? = null
    lateinit var items: MainActivity.bigItem
    var mProgressbar: ProgressBar? = null
    var isLongCLick: Boolean = false
    var isCanceled: Boolean = false

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
        textview_viewpager_head.setText(items.main_text) //기사 head
        textview_viewpager_sub.setText(items.main_text) //기사 sub
        m_textview_on_slidingdrawer.setText(items.main_text) //기사 내용. 수정

        /* ProgressBar 만들기 */
        val subitemSize = items.subitem.size
        for (i in 0..(subitemSize)) {
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                setTag("progressbar" + i)
            }
            val progressbarParams = LinearLayout.LayoutParams(progresslayout_viewpager!!.measuredWidth / subitemSize + 1, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                setMargins(10, 0, 10, 0)
                weight = 1F
            }
            progresslayout_viewpager!!.addView(progressBar, i, progressbarParams)
        }
        Log.d(TAG, "child? " + view.progresslayout_viewpager.childCount)

        /* Bottom Sheet Listener */
        var mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(mBottomSheetBehavior.state == BottomSheetBehavior.STATE_DRAGGING){
                    var alpha = Integer.toString(Math.round(slideOffset*255), 16)
                    if(alpha.length == 1) alpha = "0"+alpha
                    var newcolor = "#"+alpha + "FFFFFF"
                    btn_bottomsheet.setBackgroundColor(Color.parseColor(newcolor))
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
               // Log.d(TAG, "bottom_sheet Chenged : " + newState)
                if(newState == BottomSheetBehavior.STATE_EXPANDED){//3
                    btn_bottomsheet.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    btn_bottomsheet.setImageResource(R.drawable.ic_expand_next)
                    mAnimatorSet?.pause()
                }
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){//4
                    btn_bottomsheet.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                    btn_bottomsheet.setImageResource(R.drawable.ic_expand_less)
                    mAnimatorSet?.resume()
                }
            }
        })

        /* Bottom Sheet Click Listener */
        btn_bottomsheet.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(mBottomSheetBehavior.state  == BottomSheetBehavior.STATE_COLLAPSED){
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED //open
                }else if(mBottomSheetBehavior.state  == BottomSheetBehavior.STATE_EXPANDED){
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED //close
                }
            }
        })

        /* longClick 애니메이션 정지*/
        imageview_viewpager.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                println("long click")
                isLongCLick = true
                mAnimatorSet?.pause()
                m_pausedlayout.visibility = View.VISIBLE
                return true
            }
        })

        /* onTouch 애니메이션 재시작, 좌우 화면전환 */
        imageview_viewpager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, m: MotionEvent?): Boolean {

                when (m!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d(TAG, "ACTION_DOWN")
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d(TAG, "=====Action UP!!!====== ")
                        if (isLongCLick) { //long click, animation restart
                            mAnimatorSet?.resume()
                            view.m_pausedlayout.visibility = View.INVISIBLE
                            isLongCLick = false
                        } else { //short click
                            if (m.x > imageview_viewpager.width / 2) { //right click
                                Log.d(TAG, "short click : right " + mCurrentProgressbar + " , " + subitemSize)
                                if (mCurrentProgressbar < subitemSize + 1) {
                                    mAnimatorSet?.end()
                                }
                            } else { //left click
                                Log.d(TAG, "short click : left" + mCurrentProgressbar + " , " + subitemSize)

                                var temp = mCurrentProgressbar

                                if (temp == 0) {//change left viewpager
                                    mCurrentProgressbar = 0
                                    EventBus.getDefault().post(ViewpagerEvent(0))
                                } else if (temp > 0 && temp < subitemSize + 1) {
                                    clearAnimation()
                                    animationStart()
                                    for (i in 1..temp - 1) {
                                        println("???END ???")
                                        mAnimatorSet?.end()
                                    }
                                }
                            }
                        }
                        return false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        println("ACTION_MOVE")
                    }
                }
                return false
            }
        })
    }

    fun animationStart() {

        mAnimatorSet = setAnimation(mCurrentProgressbar)

        mAnimatorSet?.addListener(object : Animator.AnimatorListener {

            var childCount = progresslayout_viewpager.childCount
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                Log.d(TAG, " === Start Animation ===  ")
                if (mCurrentProgressbar in 1..childCount) {
                    imageview_viewpager.setImageResource(items.subitem[mCurrentProgressbar - 1].main_image)
                    textview_viewpager_sub.setText(items.subitem[mCurrentProgressbar - 1].main_text) //기사 sub
                    m_textview_on_slidingdrawer.setText(items.subitem[mCurrentProgressbar - 1].main_text) //기사 내용. 수정
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (!isCanceled) {
                    Log.d(TAG, " === End Animation ===  ")

                    //     Log.d( TAG, "current sub item num : " + currentSubItem)
                    //     Log.d( TAG,"-----Child : " +progresslayout_viewpager.javaClass)
                    //     Log.d( TAG,"childCount : " + childCount)
                    if (mCurrentProgressbar < childCount - 1) {
                        mCurrentProgressbar++
                        animationStart()

                    } else {
                        mCurrentProgressbar = 0
                        EventBus.getDefault().post(ViewpagerEvent(1))
                    }

                }
                isCanceled = false
            }

        })
        mAnimatorSet?.start()

    }

    fun setAnimation(currentSub: Int): AnimatorSet {
        mProgressbar = progresslayout_viewpager!!.findViewWithTag<ProgressBar>("progressbar" + currentSub) as ProgressBar

        var anim_progressbar = AnimatorInflater.loadAnimator(imageview_viewpager.context, R.animator.linear_progress)  //property animation방식
        anim_progressbar.setTarget(mProgressbar)

        var anim_textview = AnimatorInflater.loadAnimator(imageview_viewpager.context, R.animator.linear_background)  //property animation방식
        anim_textview.setTarget(textview_viewpager_head)
        anim_textview.setInterpolator(AccelerateDecelerateInterpolator())

        var anim_zoomin = AnimatorInflater.loadAnimator(imageview_viewpager.context, R.animator.zoom_in) as AnimatorSet  //property animation방식
        anim_zoomin.setTarget(imageview_viewpager)

        anim_zoomin.playTogether(anim_progressbar, anim_textview)

        return anim_zoomin
    }

    fun clearAnimation() {
        Log.d(TAG, " === Clear Animation ===  ")

        mCurrentProgressbar = 0
        isCanceled = true
        mAnimatorSet?.cancel()
        isCanceled = false
        mAnimatorSet?.removeAllListeners()
//        imageview_viewpager.animate().cancel()
        for (i in 0..items.subitem.size) {
            mProgressbar?.animate()?.cancel()
            progresslayout_viewpager!!.findViewWithTag<ProgressBar>("progressbar" + i).setProgress(0)
        }
        imageview_viewpager.setImageResource(items.main_image) //head이미지
        textview_viewpager_head.setText(items.main_text) //기사 head
        textview_viewpager_sub.setText(items.main_text)
        m_textview_on_slidingdrawer.setText(items.main_text) //기사 내용. 수정
    }
}