package id.shoumhome.android.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import id.shoumhome.android.activity.AlarmManagerActivity
import id.shoumhome.android.DataSessionHandler
import id.shoumhome.android.R

class BerandaFragment : Fragment() {
    var session: DataSessionHandler? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = activity?.intent?.getParcelableExtra("session")
        val txtUsername1: TextView? = getView()?.findViewById(R.id.txtNamaUser1)
        txtUsername1?.text = session?.nama_lengkap?: ""

        val btnAlarm = getView()?.findViewById<Button>(R.id.btnAlarm)
        btnAlarm?.setOnClickListener {
            val i = Intent(activity?.applicationContext, AlarmManagerActivity::class.java)
            startActivity(i)
        }
    }
}