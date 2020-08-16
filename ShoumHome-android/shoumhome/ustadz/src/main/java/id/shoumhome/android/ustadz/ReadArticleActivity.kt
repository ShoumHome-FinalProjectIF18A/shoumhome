package id.shoumhome.android.ustadz

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.viewmodels.ReadArticleViewModel
import kotlinx.android.synthetic.main.activity_read_article.*
import kotlinx.android.synthetic.main.content_read_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadArticleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var readArticleViewModel: ReadArticleViewModel
    private var id: String = ""
    private var title: String = ""
    private var content: String = ""
    private var isError: Boolean = false

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
        const val RESULT_DELETE = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_article)
        progressMessage.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE

        // Ambil Bundle Value
        val bundle = intent.extras
        id = bundle?.get(EXTRA_ARTICLE_ID).toString()

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.read_article)

        // MVVM dengan Read Article
        readArticleViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(ReadArticleViewModel::class.java)
        readArticleViewModel.getArticle().observe(this, Observer {
            if (it["status"] == true) {
                title = it["title"].toString()
                content = it["content"].toString()
                tvArticleTitle.text = it["title"].toString()
                if (it["hasImg"] == true) {
                    Glide.with(this)
                            .load(resources.getString(R.string.server) + "assets/articles/${id}.${it["extension"]}")
                            .into(imgArticle)
                } else {
                    imgArticle.visibility = View.GONE
                }
                val likes = it["like"].toString() + " ${resources.getString(R.string.likes)}"
                tvLikeCount.text = likes
                tvPostDate.text = it["post_date"].toString()
                tvPostContent.text = it["content"].toString()
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
            } else {
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
                val errorMessage = """
                    Error!
                    [${it["code"]}]: ${it["message"]}
                """.trimIndent()
                tvErrorMessage.text = errorMessage
            }
        })
        // ambil data artikel secara asinkron
        readArticleViewModel.setArticleAsync(this, id)

        // pullToRefresh
        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
            errorMessage.visibility = View.GONE
            progressMessage.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                val deferredArticles = async(Dispatchers.IO) {
                    id.let { readArticleViewModel.setArticle(applicationContext, it) }
                }
                val it = deferredArticles.await()
                if (it["status"] == true) {
                    title = it["title"].toString()
                    content = it["content"].toString()
                    tvArticleTitle.text = it["title"].toString()
                    if (it["hasImg"] == true) {
                        Glide.with(applicationContext)
                                .load(resources.getString(R.string.server) + "assets/articles/${id}.${it["extension"]}")
                                .into(imgArticle)
                    } else {
                        imgArticle.visibility = View.GONE
                    }
                    val likes = it["like"].toString() + " ${resources.getString(R.string.likes)}"
                    tvLikeCount.text = likes
                    tvPostDate.text = it["post_date"].toString()
                    tvPostContent.text = it["content"].toString()
                    progressMessage.visibility = View.GONE
                } else {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMessage = """
                            Error!
                            [${it["code"]}]: ${it["message"]}
                        """.trimIndent()
                    tvErrorMessage.text = errorMessage
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.menuEdit -> {
                if (isError) {
                    Toast.makeText(this, resources.getString(R.string.update_error), Toast.LENGTH_SHORT).show()
                } else {
                    val i = Intent(this, AddUpdateArticleActivity::class.java)
                    i.putExtra(AddUpdateArticleActivity.EXTRA_ARTICLE_ID, id)
                    i.putExtra(AddUpdateArticleActivity.EXTRA_ARTICLE_TITLE, title)
                    i.putExtra(AddUpdateArticleActivity.EXTRA_ARTICLE_CONTENT, content)
                    startActivityForResult(i, AddUpdateArticleActivity.RESULT_UPDATE)
                }
            }
            R.id.menuDelete -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(resources.getString(R.string.sure))
                        .setMessage(resources.getString(R.string.delete_article_message) + " $title?")
                        .setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, i ->
                            deleteArticle()
                        }
                        .setNegativeButton(resources.getString(R.string.no)) { dialogInterface, i ->
                            /* no-op */
                        }
                        .setCancelable(false)
                val alertDialog = alert.create()
                alertDialog.show()
            }
        }
        return true
    }

    private fun deleteArticle() {
        // sejam nyari cara karena selalu 406: NOT ACCEPTABLE, ternyata
        // request DELETE akan menolak semua parameter dalam body :(
        // kuingin marah :"v
        val url = resources.getString(R.string.server) + "api/articles/$id"
        val client = AsyncHttpClient()
        client.delete(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                Toast.makeText(this@ReadArticleActivity, resources.getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()
                this@ReadArticleActivity.setResult(RESULT_DELETE, Intent())
                finish()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                Toast.makeText(this@ReadArticleActivity,
                        "Oops, an error occured!\n[$statusCode] ${error.message}\n${responseBody.toString()}",
                        Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnRefresh -> {
                // saat tombol refresh ditekan
                errorMessage.visibility = View.GONE
                progressMessage.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredArticles = async(Dispatchers.IO) {
                        id.let { readArticleViewModel.setArticle(applicationContext, it) }
                    }
                    val it = deferredArticles.await()
                    if (it["status"] == true) {
                        title = it["title"].toString()
                        content = it["content"].toString()
                        tvArticleTitle.text = it["title"].toString()
                        if (it["hasImg"] == true) {
                            Glide.with(applicationContext)
                                    .load(resources.getString(R.string.server) + "assets/articles/${id}.${it["extension"]}")
                                    .into(imgArticle)
                        } else {
                            imgArticle.visibility = View.GONE
                        }
                        val likes = it["like"].toString() + " ${resources.getString(R.string.likes)}"
                        tvLikeCount.text = likes
                        tvPostDate.text = it["post_date"].toString()
                        tvPostContent.text = it["content"].toString()
                        progressMessage.visibility = View.GONE
                    } else {
                        progressMessage.visibility = View.GONE
                        errorMessage.visibility = View.VISIBLE
                        val errorMessage = """
                            Error!
                            [${it["code"]}]: ${it["message"]}
                        """.trimIndent()
                        tvErrorMessage.text = errorMessage
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_kajian, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == AddUpdateArticleActivity.RESULT_UPDATE) {
            errorMessage.visibility = View.GONE
            progressMessage.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                val deferredArticles = async(Dispatchers.IO) {
                    id.let { readArticleViewModel.setArticle(applicationContext, it) }
                }
                val it = deferredArticles.await()
                if (it["status"] == true) {
                    title = it["title"].toString()
                    content = it["content"].toString()
                    tvArticleTitle.text = it["title"].toString()
                    if (it["hasImg"] == true) {
                        Glide.with(applicationContext)
                                .load(resources.getString(R.string.server) + "assets/articles/${id}.${it["extension"]}")
                                .into(imgArticle)
                    } else {
                        imgArticle.visibility = View.GONE
                    }
                    val likes = it["like"].toString() + " ${resources.getString(R.string.likes)}"
                    tvLikeCount.text = likes
                    tvPostDate.text = it["post_date"].toString()
                    tvPostContent.text = it["content"].toString()
                    progressMessage.visibility = View.GONE
                } else {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMessage = """
                            Error!
                            [${it["code"]}]: ${it["message"]}
                        """.trimIndent()
                    tvErrorMessage.text = errorMessage
                }
            }
        }
    }
}