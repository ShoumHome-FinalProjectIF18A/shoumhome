package id.shoumhome.android.ustadz

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject

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
        val credentialPreference = CredentialPreference(this)
        super.onCreate(savedInstanceState)
        val waktuLoad = 4000
        setContentView(R.layout.activity_splash)
        progressBar.visibility = View.GONE
        val version = findViewById<TextView>(R.id.version)
        val getVersion = getString(R.string.app_version)
        version.text = "Versi $getVersion"
        Handler().postDelayed({
            when (sharedPreferences!!.getBoolean("first", false)) {
                true -> {
                    if (!credentialPreference.getCredential().username.isNullOrEmpty() ||
                            !credentialPreference.getCredential().password.isNullOrEmpty()) {
                        progressBar.visibility = View.VISIBLE

                        val api = resources.getString(R.string.server) + "api/ustadz/credential"
                        val client = AsyncHttpClient()

                        val params = RequestParams()
                        params.put("username", credentialPreference.getCredential().username)
                        params.put("password", credentialPreference.getCredential().password)

                        client.post(api, params, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                                val response = responseBody?.let { String(it) }
                                val responseObject = JSONObject(response!!)
                                val getUserName = responseObject.getString("username")

                                Toast.makeText(this@SplashActivity, resources.getString(R.string.welcome) + getUserName, Toast.LENGTH_SHORT).show()

                                val i = Intent(this@SplashActivity, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                                when (statusCode) {
                                    403 -> {
                                        Toast.makeText(this@SplashActivity, resources.getString(R.string.wrong_username_password), Toast.LENGTH_SHORT).show()
                                    }
                                    401 -> {
                                        Toast.makeText(this@SplashActivity, resources.getString(R.string.empty_username_password), Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(this@SplashActivity, error!!.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                val login = Intent(this@SplashActivity, LoginActivity::class.java)
                                startActivity(login)
                                finish()
                            }
                        })
                    } else {
                        val login = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(login)
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