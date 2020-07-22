package id.shoumhome.android.ustadz

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import id.shoumhome.android.ustadz.R

class SplashActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    private fun setActTheme() {
        when (sharedPreferences!!.getString("theme", "light")) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setActTheme()
        super.onCreate(savedInstanceState)
        val waktuLoad = 4000
        setContentView(R.layout.activity_splash)
        val version = findViewById<TextView>(R.id.version)
        val getVersion = getString(R.string.app_version)
        version.text = "Versi $getVersion"
        Handler().postDelayed({
            when (sharedPreferences!!.getBoolean("first", false)) {
                true -> {
                    if (!sharedPreferences!!.getString("username", "null").equals("null") ||
                            !sharedPreferences!!.getString("password", "").equals("")) {
                        val home = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(home)
                    } else {
//                        val login = Intent(this@SplashActivity, LayarUtamaActivity::class.java)
//                        startActivity(login)
                    }
                }
                false -> {
                    val intro = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intro)
                }
            }

            finish()
        }, waktuLoad.toLong())
    }
}