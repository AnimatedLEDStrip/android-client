package animatedledstrip.androidcontrol.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context, attributeSet: AttributeSet) :
    ViewPager(context, attributeSet) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false
}