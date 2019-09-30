package animatedledstrip.androidcontrol.connections

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.IPs
import kotlinx.android.synthetic.main.activity_add_connection.*
import kotlinx.android.synthetic.main.content_add_connection.*

class AddConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)
        setSupportActionBar(toolbar)

        

        IPs.forEach {
            ip_list.addView(TextView(this).apply {
                text = it
            })
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
