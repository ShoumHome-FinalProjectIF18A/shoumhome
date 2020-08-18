package id.shoumhome.android.ustadz

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.models.Ustadz
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import kotlinx.android.synthetic.main.activity_edit_identity.*
import java.util.regex.Pattern

class EditIdentityActivity : AppCompatActivity() {

    private var ustadz: Ustadz = Ustadz()

    companion object {
        const val EXTRA_PARCEL_USTADZ = "extra_parcel_ustadz"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identity)
        setFinishOnTouchOutside(false)

        rlButtons.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        val bundle = intent.extras!!
        ustadz = bundle.getParcelable(EXTRA_PARCEL_USTADZ)!!
        val name = ustadz.name
        val phone = ustadz.phone
        val address = ustadz.address
        val email = ustadz.email
        val gender = ustadz.gender

        edtUstadzName.setText(name)
        edtEmailAddress.setText(email)
        edtPhoneNumber.setText(phone)
        edtUstadzAddress.setText(address)
        if (gender == 'L') rgGender.check(R.id.rdMale) else rgGender.check(R.id.rdFemale)

        btnChange.setOnClickListener {
            var isError: Boolean = false
            if (edtUstadzName.text.toString().isBlank()) {
                isError = true
                edtUstadzName.error = resources.getString(R.string.require_to_fill)
            }

            if (edtEmailAddress.text.toString().isBlank()) {
                isError = true
                edtEmailAddress.error = resources.getString(R.string.require_to_fill)
            } else if (!isValidEmail(edtEmailAddress.text.toString())) {
                isError = true
                edtEmailAddress.error = resources.getString(R.string.invalid_email)
            }

            if (edtPhoneNumber.text.toString().isBlank()) {
                isError = true
                edtPhoneNumber.error = resources.getString(R.string.require_to_fill)
            }

            if (!isError) {
                val credential = CredentialPreference(this)
                rlButtons.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                val ustadzGender = if (rgGender.checkedRadioButtonId == R.id.rdMale) 'L' else 'P'

                val api = resources.getString(R.string.server) + "api/ustadz/identity/${credential.getCredential().username}"
                val client = AsyncHttpClient()
                val params = RequestParams()
                params.put("name", edtUstadzName.text.toString())
                params.put("phone", edtPhoneNumber.text.toString())
                params.put("gender", ustadzGender)
                params.put("address", edtUstadzAddress.text.toString())
                params.put("email", edtEmailAddress.text.toString())
                client.put(api, params, object: AsyncHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                        Toast.makeText(this@EditIdentityActivity, resources.getString(R.string.success_modify_identity), Toast.LENGTH_SHORT).show()
                        this@EditIdentityActivity.finish()
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                        Toast.makeText(this@EditIdentityActivity, resources.getString(R.string.failed_modify_identity) + error?.message, Toast.LENGTH_SHORT).show()
                        rlButtons.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                })
            }
        }
        btnCancel.setOnClickListener { finish() }
    }

    private fun isValidEmail(email: String?): Boolean {
        if (email == null) return false
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}