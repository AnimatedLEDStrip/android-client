package animatedledstrip.androidcontrol.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

/**
 * A HorizontalScrollView that asks its parent's parent to not intercept its touch events
 */
class GreedyHorizontalScrollView(context: Context, attributeSet: AttributeSet) :
    HorizontalScrollView(context, attributeSet) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        parent.parent.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }

}
