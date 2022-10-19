package animatedledstrip.androidcontrol.animation.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import kotlinx.android.synthetic.main.fragment_add_another_color.*

/**
 *
 */
class AddAnotherColorFragment(private val listener: View.OnClickListener) : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_new_color_button.setOnClickListener(listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_another_color, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: View.OnClickListener) = AddAnotherColorFragment(listener)
    }
}
