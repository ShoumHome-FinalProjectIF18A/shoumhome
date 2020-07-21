package id.shoumhome.android.activity

import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import id.shoumhome.android.R
import java.util.*

class PilihPenjualActivity : AppCompatActivity() {
    lateinit var txtLokasi: TextView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_penjual)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)!!
        txtLokasi = findViewById(R.id.txtLokasi)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            with (actionBar) {
                setDisplayHomeAsUpEnabled(true)
                setTitle("Cari Takjil")
                setSubtitle("Mau ngemil apa hari ini?")
            }
        }
        txtLokasi.text = getLocation()
    }

    fun getLocation(): String {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())

        val loc: List<String> = sharedPreferences.getString("defaultLocation", "-7.0,109.0")!!.split(",")
        val location = LatLng(loc[0].toDouble(), loc[1].toDouble())

        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addresses[0].getAddressLine(0) //0 to obtain first possible address
        val city = addresses[0].locality
        val state = addresses[0].adminArea
        val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        //create your custom title
        val full_address = "$address, $city, $state, $country, Kode Pos $postalCode"
        return full_address
    }

    fun openSettings(view: View) {
        val i: Intent = Intent(this@PilihPenjualActivity, SettingsActivity::class.java)
        startActivity(i)
    }
}