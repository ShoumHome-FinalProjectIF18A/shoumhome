package id.shoumhome.android.fragments

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import id.shoumhome.android.R
import java.util.*

class MosqueLocationMapFragment : Fragment() {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mosque_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val geocoder = Geocoder(requireContext().applicationContext, Locale.getDefault())
        val smf = activity?.supportFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment?
        smf?.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.mapType = sp.getString("map_type", "1")?.let { Integer.parseInt(it) }!!
        }
    }
}