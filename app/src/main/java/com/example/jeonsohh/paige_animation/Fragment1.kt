package com.example.jeonsohh.paige_animation

import android.os.Bundle
import android.support.design.animation.AnimationUtils
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_viewpager_item.view.*


class Fragment1 : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_viewpager_item, container, false)

//        var images = arrayOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)
//        var texts = arrayOf("test1","test2","test3","test4","test5")

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

      //  view.m_viewpager_imageview.setImageResource(items)

     //   var anim = AnimationUtils.loadAnimation(activity, R.anim.zoom_in)
     //   view.m_viewpager_imageview.startAnimation(anim)


        return view
    }

    data class bigItem(var main_image: Int, var main_text: String, var subitem: List<smallItem>)
    data class smallItem(var main_image: Int, var main_text: String)

}