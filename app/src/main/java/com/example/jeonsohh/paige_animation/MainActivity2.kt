package com.example.jeonsohh.paige_animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.Image
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*
import java.io.Serializable
import android.view.animation.DecelerateInterpolator
import android.R.attr.animation
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout


class MainActivity2 : AppCompatActivity() {

    companion object {
        lateinit var animatorSet : AnimatorSet
        var currentSubItem : Int = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_viewpager.adapter = myViewPagerAdapter(supportFragmentManager)
        m_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                println("viewpager child count : " + m_viewpager.childCount)

                var view = m_viewpager.getChildAt(position).findViewWithTag<ImageView>("imageview") as ImageView
                var mprogressbar = m_viewpager.getChildAt(position).findViewWithTag<ProgressBar>("progressbar"+currentSubItem)

                var anim_progressbar = ObjectAnimator.ofInt(mprogressbar, "progress", 0, 100).apply {
                    setDuration(7000)
                    setInterpolator(LinearInterpolator())
                } //progressbar animation

                var anim_zoomin = AnimatorInflater.loadAnimator(view.context, R.animator.zoom_in) as AnimatorSet  //property animation방식
                anim_zoomin.setTarget(view)
                //imageview animation

                animatorSet = AnimatorSet()
                animatorSet.playTogether(anim_progressbar, anim_zoomin)
                animatorSet.start()

                currentSubItem = 0
                animatorSet.addListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                     //   var childCount = m_viewpager.getChildAt(position).findViewById<LinearLayout>(R.id.m_progresslayout).childCount
                        println("-----Child : " +m_viewpager.findViewById<LinearLayout>(R.id.m_progresslayout).javaClass)
                        println("childCount : " + childCount)
                        if (currentSubItem < childCount){
                            currentSubItem++
                        }else{
                            currentSubItem = 0
                        }
                        animatorSet.start()

                    }

                })
            }


        })

    }


    class myViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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

        //페이지 갯수 세는 부분
        override fun getCount(): Int {
            return items.size
        }

        //화면 넣어주는 부분
        override fun getItem(position: Int): Fragment? {
            var bundle: Bundle = Bundle(1)

            when (position) {
                0 -> {
                    val fragment = ViewerFragment()
                    bundle.putSerializable("items", items[0])
                    fragment.arguments = bundle
                    return fragment
                }
                1 -> {
                    val fragment = ViewerFragment()
                    bundle.putSerializable("items", items[1])
                    fragment.arguments = bundle
                    return fragment
                }

                else -> return null
            }
        }

    }

    data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>) : Serializable
    data class smallItem(var main_image: Int, var main_text: String)


}

