package id.shoumhome.android.ustadz

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            // hide button, show loading
            btnLogin.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val credentialPreference = CredentialPreference(this)
            val api = resources.getString(R.string.server) + "api/ustadz/credential"
            val client = AsyncHttpClient()

            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            val params = RequestParams()
            params.put("username", username)
            params.put("password", password)

            client.post(api, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    val credential = Credential()
                    credential.username = username
                    credential.password = password
                    credentialPreference.setCredential(credential)

                    val response = responseBody?.let { String(it) }
                    val responseObject = JSONObject(response!!)
                    val getUserName = responseObject.getString("username")

                    Toast.makeText(this@LoginActivity, resources.getString(R.string.welcome) + getUserName, Toast.LENGTH_SHORT).show()

                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    btnLogin.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    when (statusCode) {
                        403 -> {
                            Toast.makeText(this@LoginActivity, resources.getString(R.string.wrong_username_password), Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            Toast.makeText(this@LoginActivity, resources.getString(R.string.empty_username_password), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@LoginActivity, error!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

}