package com.example.jeonsohh.paige_animation

import android.animation.AnimatorSet
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    var mAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //소분류
        var subBubble = listOf<smallItem>(smallItem(R.drawable.image1_bubble1, "image1_bubble1"),
                smallItem(R.drawable.image1_bubble2, "image1_bubble2"),
                smallItem(R.drawable.image1_bubble3, "image1_bubble3"))
        //대분류
        var bubble = bigItem(R.drawable.image1, "image1", subBubble)

        var subBridge = listOf<smallItem>(smallItem(R.drawable.image2_bridge1, "image2_bridge1"),
                smallItem(R.drawable.image2_bridge2, "image2_bridge2"))
        var bridge = bigItem(R.drawable.image2, "image2", subBridge)

        var items: MutableList<ViewerFragment> = arrayListOf()

        val bundle1 = Bundle()
        val fragment1 = ViewerFragment()
        bundle1.putSerializable("items", bubble)
        fragment1.arguments = bundle1
        items.add(fragment1)

        val bundle2 = Bundle()
        val fragment2 = ViewerFragment()
        bundle2.putSerializable("items", bridge)
        fragment2.arguments = bundle2
        items.add(fragment2)


        mAdapter = ViewPagerAdapter(items, supportFragmentManager)
        viewpager_main.adapter = mAdapter
        viewpager_main.offscreenPageLimit = 2
        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                var fragment = mAdapter?.getItem(position) as ViewerFragment
                fragment.animationStart()
            }


        })


    }

    class ViewPagerAdapter(val items: MutableList<ViewerFragment>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        //페이지 갯수 세는 부분
        override fun getCount(): Int {
            return items.size
        }

        //화면 넣어주는 부분
        override fun getItem(position: Int): Fragment? {
            return items[position]
        }

    }

    data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>) : Serializable
    data class smallItem(var main_image: Int, var main_text: String)


}

