package animatedledstrip.androidcontrol.stripinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import kotlinx.android.synthetic.main.fragment_strip_info_container.*

class StripInfoContainer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strip_info_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager
            .beginTransaction()
            .add(
                strip_info_container.id,
                StripInfoFragment(),
                "strip info"
            )
            .add(
                strip_info_container.id,
                StripColorFragment(),
                "strip color"
            )
            .commit()
    }
}