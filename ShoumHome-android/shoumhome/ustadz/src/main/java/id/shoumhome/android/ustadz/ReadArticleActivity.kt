package id.shoumhome.android.ustadz

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.shoumhome.android.ustadz.viewmodels.ReadArticleViewModels
import kotlinx.android.synthetic.main.activity_read_article.*
import kotlinx.android.synthetic.main.content_read_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadArticleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var readArticleViewModels: ReadArticleViewModels
    private var id: String = ""

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
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
        readArticleViewModels = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(ReadArticleViewModels::class.java)
        readArticleViewModels.getArticle().observe(this, Observer {
            if (it["status"] == true) {
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
        readArticleViewModels.setArticleAsync(this, id)

        // pullToRefresh
        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
            errorMessage.visibility = View.GONE
            progressMessage.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                val deferredArticles = async(Dispatchers.IO) {
                    id.let { readArticleViewModels.setArticle(applicationContext, it) }
                }
                val it = deferredArticles.await()
                if (it["status"] == true) {
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
        }
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnRefresh -> {
                // saat tombol refresh ditekan
                errorMessage.visibility = View.GONE
                progressMessage.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredArticles = async(Dispatchers.IO) {
                        id.let { readArticleViewModels.setArticle(applicationContext, it) }
                    }
                    val it = deferredArticles.await()
                    if (it["status"] == true) {
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
        menuInflater.inflate(R.menu.menu_read_article, menu)
        return true
    }
}