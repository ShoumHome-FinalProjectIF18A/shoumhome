package id.shoumhome.android.activity

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.ThemeUtils
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.shoumhome.android.R
import id.shoumhome.android.pager.MosqueLocationPagerAdapter
import kotlinx.android.synthetic.main.activity_mosque_location.*

class MosqueLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mosque_location)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.elevation = 1f

        val mosqueLocationPagerAdapter = MosqueLocationPagerAdapter(this, supportFragmentManager)
        vpMosqueLocation.adapter = mosqueLocationPagerAdapter
        tlMosqueLocation.setupWithViewPager(vpMosqueLocation)
//        tlMosqueLocation.getTabAt(0)?.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_location_city_24, theme)
//        tlMosqueLocation.getTabAt(1)?.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_list_24, theme)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}