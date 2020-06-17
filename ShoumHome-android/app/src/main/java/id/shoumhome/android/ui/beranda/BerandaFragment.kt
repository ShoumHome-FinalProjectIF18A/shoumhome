package id.shoumhome.android.ui.beranda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
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
    }
}