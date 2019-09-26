package animatedledstrip.androidcontrol.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class LockableScrollView(context: Context, attributeSet: AttributeSet) : ScrollView(context, attributeSet) {

    var scrollingEnabled = true

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
        if (scrollingEnabled) super.onInterceptTouchEvent(ev) else false

    override fun onTouchEvent(ev: MotionEvent?) =
        if (scrollingEnabled) super.onTouchEvent(ev) else false

}