package com.example.jeonsohh.paige_animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_viewpager_item.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        m_viewpager.adapter = myViewPagerAdapter()

    }


}
