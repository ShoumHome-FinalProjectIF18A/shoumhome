package id.shoumhome.android.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.databases.articles.DatabaseContract
import id.shoumhome.android.databases.articles.DbArticleHelper
import id.shoumhome.android.databases.articles.MappingHelper
import id.shoumhome.android.models.Article
import id.shoumhome.android.viewmodels.ReadArticleViewModel
import kotlinx.android.synthetic.main.activity_read_article.*
import kotlinx.android.synthetic.main.content_read_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadArticleActivity : AppCompatActivity() {
    private lateinit var readArticleViewModel: ReadArticleViewModel
    private var id: String = ""
    private var title: String = ""
    private var content: String = ""
    private var isError: Boolean = false
    private var downloaded = false
    private lateinit var dbArticleHelper: DbArticleHelper
    private lateinit var article: Article

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
                article = Article()
                article.id = it["id"].toString()
                article.title = it["title"].toString()
                article.content = it["content"].toString()
                article.post_date = it["post_date"].toString()
                article.ustadzName = it["ustadz_name"].toString()
                article.hasImg = it["hasImg"].toString()
                article.imgUrl = ""
                article.likes = it["like"].toString().toInt()
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
        dbArticleHelper= DbArticleHelper.getInstance(this)
        dbArticleHelper.open()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredArticle = async(Dispatchers.IO) {
                val cursor = dbArticleHelper.queryById(id)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val isDownload = deferredArticle.await()
            if(isDownload.size==0){
                downloaded= false
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_returned_24)
            }else{
                downloaded= true
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_turned_in_24)
            }
        }
        fabDownload.setOnClickListener {
            if(downloaded){
                dbArticleHelper.deleteById(id)
                downloaded= false
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_returned_24)
                Toast.makeText(this, "Artikel telah dihapus!",Toast.LENGTH_LONG).show()
            }else{
                val values= ContentValues()
                values.put(DatabaseContract.ArticleColums.ID, article.id)
                values.put(DatabaseContract.ArticleColums.TITLE, article.title)
                values.put(DatabaseContract.ArticleColums.POST_DATE, article.post_date)
                values.put(DatabaseContract.ArticleColums.CONTENT, article.content)
                values.put(DatabaseContract.ArticleColums.HAS_IMG, article.hasImg)
                values.put(DatabaseContract.ArticleColums.USTADZ_NAME, article.ustadzName)
                values.put(DatabaseContract.ArticleColums.IMGURL, article.imgUrl)
                dbArticleHelper.insert(values)
                downloaded= true
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_turned_in_24)
                Toast.makeText(this, "Artikel Berhasil Diunduh", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}

