package id.shoumhome.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import id.shoumhome.android.DataSessionHandler
import id.shoumhome.android.R

class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    var doubleBackToExitPressedOnce = false
    private var session: DataSessionHandler? = null

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar dari ShoumHome!", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi variabel
        session = intent.getParcelableExtra("session")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // Buat actionbar yang berisi toolbar
        setSupportActionBar(toolbar)

        // Floating Action Button
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_downloaded_articles, R.id.nav_saved_kajian)
                .setDrawerLayout(drawer)
                .build()

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
        val headerView = navigationView?.getHeaderView(0)
        val txtUsername: TextView? = headerView?.findViewById(R.id.txtNamaUser2)
        val txtEmail: TextView? = headerView?.findViewById(R.id.txtEmail)
        txtUsername?.text = session?.nama_lengkap
        txtEmail?.text = session?.email
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAbout) {
            val i = Intent(this, AboutActivity::class.java)
            startActivity(i)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onResume() {
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        when (sp.getString("theme", "auto")) {
            "auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        // Ambil parameter dari data sebelumnya

        // bug
//        val txtUsername2: TextView = findViewById(R.id.txtNamaUser2)
//        txtUsername2.text = session?.nama_lengkap?: ""

        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    fun openKajianOnline(view: View?) {
        val i = Intent(".KajianActivity")
        startActivity(i)
    }

    fun openArtikelIslami(view: View?) {
        val i = Intent(this, ArtikelActivity::class.java)
        startActivity(i)
    }

    fun openMakanMinum(view: View?) {
        val i = Intent(this@MainActivity, PilihPenjualActivity::class.java)
        startActivity(i)
    }
    fun openLokasiMasjid(view: View) {}
}