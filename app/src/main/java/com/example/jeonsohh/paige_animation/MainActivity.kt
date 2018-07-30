package com.example.jeonsohh.paige_animation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    var mAdapter: ViewPagerAdapter? = null
    var mIsFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* full screen */
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

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

        var subCat = listOf<smallItem>(smallItem(R.drawable.image3_cat1, "image3_cat1"),
                smallItem(R.drawable.image3_cat2, "image3_cat2"),
                smallItem(R.drawable.image3_cat3, "image3_cat3"),
                smallItem(R.drawable.image3_cat4, "image4_cat4"),
                smallItem(R.drawable.image3_cat5, "image3_cat5"))
        var cat = bigItem(R.drawable.image3, "image3", subCat)

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

        val bundle3 = Bundle()
        val fragment3 = ViewerFragment()
        bundle3.putSerializable("items", cat)
        fragment3.arguments = bundle3
        items.add(fragment3)

        mAdapter = ViewPagerAdapter(items, supportFragmentManager)
        viewpager_main.adapter = mAdapter
        viewpager_main.offscreenPageLimit = items.size
        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(mIsFirst && positionOffset == 0f && positionOffsetPixels == 0){
                    onPageSelected(0)
                    mIsFirst = false
                }

            }
            override fun onPageSelected(position: Int) {
                var fragment = mAdapter?.getItem(position) as ViewerFragment //item[position]
                if(!mIsFirst) {
                    for (i in 0..items.size - 1) {
                        items[i].clearAnimation()
                    }
                }
                fragment.animationStart()
            }

        })

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeCurrentPage(event : ViewpagerEvent){
        if(event.direction == 1){ //right
            if(viewpager_main.currentItem < viewpager_main.childCount-1){
                viewpager_main.setCurrentItem(++viewpager_main.currentItem)
            }else if(viewpager_main.currentItem == viewpager_main.childCount-1){
                viewpager_main.setCurrentItem(0)
            }
        }else if(event.direction == 0){
            if(viewpager_main.currentItem > 0){
                viewpager_main.setCurrentItem(--viewpager_main.currentItem)
            }else if(viewpager_main.currentItem == 0){
                viewpager_main.setCurrentItem(viewpager_main.childCount-1)

            }
        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
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

