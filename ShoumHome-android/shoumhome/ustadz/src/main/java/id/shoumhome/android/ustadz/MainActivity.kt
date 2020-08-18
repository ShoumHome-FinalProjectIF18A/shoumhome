package id.shoumhome.android.ustadz

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSettings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
            }
            R.id.menuAbout -> {
                val i = Intent(this, AboutActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
