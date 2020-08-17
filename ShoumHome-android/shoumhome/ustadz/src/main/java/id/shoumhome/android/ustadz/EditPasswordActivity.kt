package id.shoumhome.android.ustadz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.models.Credential
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import kotlinx.android.synthetic.main.activity_edit_password.*

class EditPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)
        setFinishOnTouchOutside(false)

        btnChange.setOnClickListener {
            val credentialPreference = CredentialPreference(this)
            val oldPassword = credentialPreference.getCredential().password

            edtOldPassword.error = null
            edtNewPassword.error = null
            edtConfirmPassword.error = null

            if (edtOldPassword.text.toString() == oldPassword) {
                if (edtNewPassword.text.toString().length < 6) {
                    // new password kurang dari 6
                    edtNewPassword.error = resources.getString(R.string.new_password_not_meet_minimum)
                }
                if (edtConfirmPassword.text.toString().length < 6) {
                    // confirm password kurang dari 6
                    edtConfirmPassword.error = resources.getString(R.string.confirm_password_not_meet_minimum)
                }
                if ((edtNewPassword.text.toString().length >= 6) &&
                        (edtConfirmPassword.text.toString().length >= 6)) {
                    if (edtNewPassword.text.toString() == edtConfirmPassword.text.toString()) {
                        rlButtons.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE

                        val api = resources.getString(R.string.server) + "api/ustadz/credential"
                        val client = AsyncHttpClient()
                        val params = RequestParams()
                        params.put("username", credentialPreference.getCredential().username)
                        params.put("old_pass", edtOldPassword.text.toString())
                        params.put("new_pass", edtNewPassword.text.toString())

                        client.put(api, params, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                                val credential = Credential()
                                credential.username = credentialPreference.getCredential().username
                                credential.password = edtNewPassword.text.toString()
                                credentialPreference.setCredential(credential)

                                Toast.makeText(this@EditPasswordActivity, resources.getString(R.string.success_change_pass), Toast.LENGTH_SHORT).show()
                                finish()
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                                Toast.makeText(this@EditPasswordActivity, resources.getString(R.string.failed_change_pass) + error?.message, Toast.LENGTH_LONG).show()
                            }
                        })
                    } else {
                        // new password and confirm password isn't same
                        edtConfirmPassword.error = resources.getString(R.string.confirm_password_not_same)
                    }
                }
            } else {
                edtOldPassword.error = resources.getString(R.string.wrong_old_pass)
            }
        }

        btnCancel.setOnClickListener { finish() }
    }
}