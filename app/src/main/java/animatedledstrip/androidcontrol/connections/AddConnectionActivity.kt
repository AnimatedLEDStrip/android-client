package animatedledstrip.androidcontrol.connections

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.IPs
import kotlinx.android.synthetic.main.activity_add_connection.*
import kotlinx.android.synthetic.main.content_add_connection.*

class AddConnectionActivity : AppCompatActivity(), ServerEditFragment.ServerEditListener {
    override fun onDialogPositiveClick(dialog: DialogFragment, oldIp: String, newIp: String) {
        val index = IPs.indexOf(oldIp)
        IPs.remove(oldIp)
        if (index == -1) IPs.add(newIp) else IPs.add(index, newIp)
        loadServers()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    override fun onRemoveClick(dialog: DialogFragment, ip: String) {
        IPs.remove(ip)
        loadServers()
    }

    private fun showEditDialog() {
        val dialog = ServerEditFragment("")
        dialog.show(supportFragmentManager, "ServerEditFragment")
    }

    private fun loadServers() {
        ip_list.removeAllViews()
        IPs.forEach { ip ->
            supportFragmentManager.beginTransaction()
                .add(
                    ip_list.id,
                    ServerFragment.newInstance(ip),
                    ip
                )
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)
        setSupportActionBar(toolbar)

        loadServers()
        fab.setOnClickListener {
            showEditDialog()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
