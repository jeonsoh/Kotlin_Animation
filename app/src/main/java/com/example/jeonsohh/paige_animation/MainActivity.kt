package com.example.jeonsohh.paige_animation

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_viewpager_item.view.*

import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      //  m_viewpager.adapter = myViewPagerAdapter(supportFragmentManager)
        m_viewpager.adapter = myViewPagerAdapter(applicationContext)

    }



    class myViewPagerAdapter(context : Context) : PagerAdapter(){
        //소분류
        var subBubble = listOf<smallItem>( smallItem(R.drawable.image1_bubble1,"image1_bubble1"),
                smallItem(R.drawable.image1_bubble2, "image1_bubble2"),
                smallItem(R.drawable.image1_bubble3,"image1_bubble3") )
        //대분류
        var bubble = bigItem(R.drawable.image1, "image1", subBubble)

        var subBridge = listOf<smallItem>( smallItem(R.drawable.image2_bridge1, "image2_bridge1"),
                smallItem(R.drawable.image2_bridge2, "image2_bridge2") )
        var bridge = bigItem(R.drawable.image2, "image2", subBridge)

        var items = arrayOf( bubble, bridge)

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view.equals(`object`)
        }

        override fun getCount(): Int {
            return  items.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var view = LayoutInflater.from(container.context).inflate(R.layout.activity_viewpager_item, container, false)
            view.m_viewpager_imageview.setImageResource(items[position].main_image)

            var anim = AnimationUtils.loadAnimation(container.context, R.anim.zoom_in)
            view.m_viewpager_imageview.startAnimation(anim)
            container.addView(view)
            return view
        }

    }
//    class myViewPagerAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm){
//
//       // var fragments = arrayOf(Fragment1(), Fragment1(),Fragment1())
//       //소분류
//       var subBubble = listOf<smallItem>( smallItem(R.drawable.image1_bubble1,"image1_bubble1"),
//               smallItem(R.drawable.image1_bubble2, "image1_bubble2"),
//               smallItem(R.drawable.image1_bubble3,"image1_bubble3") )
//        //대분류
//        var bubble = bigItem(R.drawable.image1, "image1", subBubble)
//
//        var subBridge = listOf<smallItem>( smallItem(R.drawable.image2_bridge1, "image2_bridge1"),
//                smallItem(R.drawable.image2_bridge2, "image2_bridge2") )
//        var bridge = bigItem(R.drawable.image2, "image2", subBridge)
//
//        var items = arrayOf( bubble, bridge)
//
//
//        override fun getItem(position: Int): Fragment {
//            return Fragment1
//
//        }
//
//        override fun getCount(): Int {
//            return  items.size
//
//        }
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            val view = LayoutInflater.from(this).inflate(R.layout.activity_viewpager_item, container, false)
//
//        }
//    }

    data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>)
    data class smallItem(var main_image: Int, var main_text: String)

}
