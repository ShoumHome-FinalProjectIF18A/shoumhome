package id.shoumhome.android.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import id.shoumhome.android.DataSessionHandler
import id.shoumhome.android.R
import id.shoumhome.android.activity.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {
    private var session: DataSessionHandler? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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

        // Button
        btnFood.setOnClickListener(this)
        btnArticle.setOnClickListener(this)
        btnKajian.setOnClickListener(this)
        btnMosqueLocation.setOnClickListener(this)
        btnCalc.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnFood -> {
                val i = Intent(context, PilihPenjualActivity::class.java)
                startActivity(i)
            }
            R.id.btnArticle -> {
                val i = Intent(context, ArtikelActivity::class.java)
                startActivity(i)
            }
            R.id.btnKajian -> {
                val i = Intent(context, KajianActivity::class.java)
                startActivity(i)
            }
            R.id.btnMosqueLocation -> {
                val i = Intent(context, MosqueLocationActivity::class.java)
                startActivity(i)
            }
            R.id.btnCalc -> {
                val i = Intent(context, ZakatActivity::class.java)
                startActivity(i)
            }
        }
    }
}