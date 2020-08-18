package id.shoumhome.android.ustadz

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.models.Credential
import id.shoumhome.android.ustadz.models.Ustadz
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MAIN_ACTIVITY = "extra_main_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val theme = findPreference<ListPreference>("theme")
            val changePassword = findPreference<Preference>("changePassword")
            val changeIdentity = findPreference<Preference>("changeIdentity")
            val logout = findPreference<Preference>("logout")

            theme?.setOnPreferenceChangeListener { preference, newValue ->
                when (newValue.toString()) {
                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }

            changeIdentity?.setOnPreferenceClickListener {
                val credentialPreference = CredentialPreference(requireContext())
                Snackbar.make(requireView().rootView, resources.getString(R.string.please_wait), BaseTransientBottomBar.LENGTH_SHORT).show()
                val api = resources.getString(R.string.server) + "api/ustadz/identity/${credentialPreference.getCredential().username}"
                val client = AsyncHttpClient()
                client.get(api, object: AsyncHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                        val result = responseBody?.let { String(it) }
                        val resultObject = JSONObject(result!!)
                        val ustadz = Ustadz()
                        ustadz.name = resultObject.getString("name")
                        ustadz.address = resultObject.getString("address")
                        ustadz.email = resultObject.getString("email")
                        ustadz.phone = resultObject.getString("phone")
                        ustadz.gender = resultObject.getString("gender").single()
                        val i = Intent(context, EditIdentityActivity::class.java)
                        i.putExtra(EditIdentityActivity.EXTRA_PARCEL_USTADZ, ustadz)
                        startActivity(i)
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                        Snackbar.make(requireView().rootView, resources.getString(R.string.failed_load_data), BaseTransientBottomBar.LENGTH_SHORT).show()
                    }
                })
                true
            }

            changePassword?.setOnPreferenceClickListener {
                val i = Intent(context, EditPasswordActivity::class.java)
                startActivity(i)
                true
            }

            logout?.setOnPreferenceClickListener {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle(resources.getString(R.string.sure))
                alert.setMessage(resources.getString(R.string.logout_confirm))
                alert.setPositiveButton(resources.getString(R.string.yes)) { a, b ->
                    val credentialPreference = CredentialPreference(requireActivity().applicationContext!!)
                    val credential = Credential()
                    credential.username = ""
                    credential.password = ""
                    credentialPreference.setCredential(credential)
                    Toast.makeText(context, resources.getString(R.string.goodbye), Toast.LENGTH_SHORT).show()

                    val i = Intent(context, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i)
                    requireActivity().finishAffinity()
                }
                alert.setNegativeButton(resources.getString(R.string.no)) { a, b ->
                    /* no-op */
                }
                val alertDialog = alert.create()
                alertDialog.show()

                true
            }
        }

    }
}