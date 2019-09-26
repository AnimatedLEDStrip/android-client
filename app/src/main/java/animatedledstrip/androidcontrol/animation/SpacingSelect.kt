package animatedledstrip.androidcontrol.animation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R

/**
 * A simple [Fragment] subclass.
 */
class SpacingSelect : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spacing_select, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SpacingSelect()
    }

}
