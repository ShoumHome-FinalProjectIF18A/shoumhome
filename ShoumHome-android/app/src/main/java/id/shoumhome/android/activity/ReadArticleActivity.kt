package id.shoumhome.android.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.viewmodels.ReadArticleViewModel
import kotlinx.android.synthetic.main.activity_read_article.*
import kotlinx.android.synthetic.main.content_read_article.*

class ReadArticleActivity : AppCompatActivity() {
    private lateinit var readArticleViewModel: ReadArticleViewModel
    private var id: String = ""
    private var title: String = ""
    private var content: String = ""
    private var isError: Boolean = false

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_article)

        // Ambil Bundle Value
        val bundle = intent.extras
        id = bundle?.get(EXTRA_ARTICLE_ID).toString()

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.read_article)

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

        readArticleViewModel.setArticleAsync(this, id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_artikel, menu)
        return super.onCreateOptionsMenu(menu)
    }

}

