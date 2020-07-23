package id.shoumhome.android.ustadz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.shoumhome.android.ustadz.pager.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TabLayout
        val mainPagerAdapter = MainPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = mainPagerAdapter
        tabs.setupWithViewPager(view_pager)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = getString(R.string.app_name)
    }
}
