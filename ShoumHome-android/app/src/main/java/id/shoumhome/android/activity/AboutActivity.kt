package id.shoumhome.android.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import id.shoumhome.android.R
import id.shoumhome.android.pager.AboutPagerAdapter
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val aboutPagerAdapter = AboutPagerAdapter(this, supportFragmentManager)
        vpAbout.adapter = aboutPagerAdapter
        tabs.setupWithViewPager(vpAbout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.menuGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/ShoumHome-FinalProjectIF18A")
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}