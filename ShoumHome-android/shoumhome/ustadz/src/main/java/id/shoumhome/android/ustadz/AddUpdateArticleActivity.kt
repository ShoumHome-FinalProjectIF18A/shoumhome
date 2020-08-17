package id.shoumhome.android.ustadz

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import kotlinx.android.synthetic.main.activity_add_update_article.*

class AddUpdateArticleActivity : AppCompatActivity() {

    private var id: String? = ""
    private var title: String? = ""
    private var content: String? = ""
    private var isProgress: Boolean = false

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
        const val EXTRA_ARTICLE_TITLE = "extra_article_title"
        const val EXTRA_ARTICLE_CONTENT = "extra_article_content"

        // Request Code
        const val RESULT_SAVE = 130
        const val RESULT_UPDATE = 260
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_article)
        progressMessage.visibility = View.GONE
        errorMessage.visibility = View.GONE

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        id = bundle?.getString(EXTRA_ARTICLE_ID, "")
        title = bundle?.getString(EXTRA_ARTICLE_TITLE, "")
        content = bundle?.getString(EXTRA_ARTICLE_CONTENT, "")

        var titleWindow = ""
        if (id.isNullOrEmpty()) {
            titleWindow = resources.getString(R.string.add_article)
            edtArticleTitle.text.clear()
            edtArticleContent.text.clear()
        } else {
            titleWindow = resources.getString(R.string.update_article)
            edtArticleTitle.setText(title)
            edtArticleContent.setText(content)
        }
        supportActionBar?.title = titleWindow

        btnCancelError.setOnClickListener {
            isProgress = false
            progressMessage.visibility = View.GONE
            errorMessage.visibility = View.GONE
        }

        btnRetryError.setOnClickListener {
            saveArticle()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_update, menu)
        return true
    }

    override fun onBackPressed() {
        if (!isProgress) super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.menuSave -> saveArticle()
        }
        return true
    }

    private fun saveArticle() {
        if (id.isNullOrEmpty()) {
            isProgress = true

            // simpan artikel baru
            progressMessage.visibility = View.VISIBLE
            tvProgressMessage.setText(R.string.save_please_wait)
            errorMessage.visibility = View.GONE

            // Ambil data dari EditText
            val articleTitle = edtArticleTitle.text.toString()
            val articleContent = edtArticleContent.text.toString()

            if (articleTitle == "") {
                Toast.makeText(this, resources.getString(R.string.title_must_fill), Toast.LENGTH_SHORT).show()
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                isProgress = false
                return
            }

            if (articleContent == "") {
                Toast.makeText(this, resources.getString(R.string.content_must_fill), Toast.LENGTH_SHORT).show()
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                isProgress = false
                return
            }

            val url = resources.getString(R.string.server) + "api/articles"
            val credential = CredentialPreference(this)
            val client = AsyncHttpClient()

            // Request Parameter di endpoint /api/articles
            val params = RequestParams()
            params.put("title", articleTitle)
            params.put("content", articleContent)
            params.put("ustadz_id", credential.getCredential().username)

            client.post(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this@AddUpdateArticleActivity, resources.getString(R.string.save_success), Toast.LENGTH_SHORT).show()

                    this@AddUpdateArticleActivity.setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMsg = "Oops! An error occurred.\n[$statusCode] ${error.message}"
                    tvErrorMessage.text = errorMsg
                }
            })
        } else {
            isProgress = true

            // simpan artikel baru
            progressMessage.visibility = View.VISIBLE
            tvProgressMessage.setText(R.string.save_please_wait)
            errorMessage.visibility = View.GONE

            // Ambil data dari EditText
            val articleTitle = edtArticleTitle.text.toString()
            val articleContent = edtArticleContent.text.toString()

            if (articleTitle == "") {
                Toast.makeText(this, resources.getString(R.string.title_must_fill), Toast.LENGTH_SHORT).show()
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                isProgress = false
                return
            }

            if (articleContent == "") {
                Toast.makeText(this, resources.getString(R.string.content_must_fill), Toast.LENGTH_SHORT).show()
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                isProgress = false
                return
            }

            val url = resources.getString(R.string.server) + "api/articles/$id"
            val client = AsyncHttpClient()

            // Request Parameter di endpoint /api/articles
            val params = RequestParams()
            params.put("title", articleTitle)
            params.put("content", articleContent)

            client.put(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this@AddUpdateArticleActivity, resources.getString(R.string.update_success), Toast.LENGTH_SHORT).show()

                    this@AddUpdateArticleActivity.setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMsg = "Oops! An error occurred.\n[$statusCode] ${error.message}"
                    tvErrorMessage.text = errorMsg
                }
            })
        }
    }
}