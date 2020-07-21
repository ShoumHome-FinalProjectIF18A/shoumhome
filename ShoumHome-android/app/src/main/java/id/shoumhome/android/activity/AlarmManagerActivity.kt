package id.shoumhome.android.activity

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import com.google.android.gms.maps.model.LatLng
import id.shoumhome.android.R
import kotlinx.android.synthetic.main.activity_alarm_manager.*
import java.util.*

class AlarmManagerActivity : AppCompatActivity() {

    private var lintang: Double = 0.0
    private var bujur: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_manager)
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val defaultLocation = sp.getString("defaultLocation", "-7.0,109.0")?.split(",")

        lintang = defaultLocation?.get(0).toString().toDouble()
        bujur = defaultLocation?.get(1).toString().toDouble()

        tvLintang.text = lintang.toString()
        tvBujur.text = bujur.toString()

        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val location = LatLng((tvLintang.text as String).toDouble(), (tvBujur.text as String).toDouble())

        val addressList: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addressList[0].getAddressLine(0)
        val city = addressList[0].locality
        val state = addressList[0].adminArea
        val country = addressList[0].countryName
        val postalCode = addressList[0].postalCode

        tvAlamat.text = "$address, $city, $state, $country"
        tvKodePos.text = postalCode

        // fungsi Toolbar dan ActionBar
        val tb = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(tb)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Alarm Sahur")
        supportFragmentManager.beginTransaction().replace(R.id.settings, AlarmConfigFragment()).commit()

        // Get Imsak Times
        val today = SimpleDate(GregorianCalendar())
        val location_for_azan = Location(lintang, bujur, 7.0, 0)
        val azan = Azan(location_for_azan, Method.EGYPT_SURVEY)
        val imsak = azan.getImsaak(today)
        tvWaktuImsak.text = imsak.toString()
    }

    class AlarmConfigFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.alarm_preferences, rootKey)
        }
    }
}