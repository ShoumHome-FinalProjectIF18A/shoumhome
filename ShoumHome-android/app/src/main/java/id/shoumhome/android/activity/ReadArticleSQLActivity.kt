package id.shoumhome.android.activity

import android.content.ContentValues
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.gms.common.internal.ConnectionErrorMessages
import id.shoumhome.android.R
import id.shoumhome.android.databases.articles.DatabaseContract
import id.shoumhome.android.databases.articles.DbArticleHelper
import id.shoumhome.android.databases.articles.MappingHelper
import id.shoumhome.android.models.Article
import kotlinx.android.synthetic.main.activity_read_article.*
import kotlinx.android.synthetic.main.content_read_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadArticleSQLActivity: AppCompatActivity() {
    private lateinit var article: Article
    private var downloaded = false
    private lateinit var dbArticleHelper: DbArticleHelper


    companion object {
        const val EXTRA_PARCEL_ARTICLES = "extra_parcel_articles"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_article)

        //menutup layar progres dan error
        progressMessage.visibility = View.GONE
        errorMessage.visibility = View.GONE
        imgLike.visibility = View.GONE
        tvLikeCount.visibility = View.GONE

        val bundle = intent.extras!!
        article = bundle.getParcelable(EXTRA_PARCEL_ARTICLES)!!
        tvArticleTitle.text = article.title
        tvPostDate.text = article.post_date
        val ustadzName = resources.getString(R.string.by) + " " + article.ustadzName
        tv_nama_Ustad.text = ustadzName
        tvPostContent.text = article.content

        if (article.hasImg == "true") {
            Glide.with(this)
                    .load(article.imgUrl) //masih bug
                    .into(imgArticle)
        } else {
            imgArticle.visibility = View.GONE
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.read_article)

        dbArticleHelper = DbArticleHelper.getInstance(this)
        dbArticleHelper.open()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredArticle = async(Dispatchers.IO) {
                val cursor = article.id?.let { dbArticleHelper.queryById(it) }
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val isDownload = deferredArticle.await()
            if (isDownload.size == 0) {
                downloaded = false
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_returned_24)
            } else {
                downloaded = true
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_turned_in_24)
            }

        }
        fabDownload.setOnClickListener {
            if (downloaded) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(resources.getString(R.string.sure))
                alert.setMessage(resources.getString(R.string.remove_article_confirm))
                alert.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    article.id?.let { it1 -> dbArticleHelper.deleteById(it1) }
                    downloaded = false
                    fabDownload.setImageResource(R.drawable.ic_baseline_assignment_returned_24)
                    Toast.makeText(this, "Artikel telah dihapus!", Toast.LENGTH_LONG).show()
                    finish()
                }
                alert.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                    /* no-op */
                }
                alert.create().show()
            } else {
                val values = ContentValues()
                values.put(DatabaseContract.ArticleColums.ID, article.id)
                values.put(DatabaseContract.ArticleColums.TITLE, article.title)
                values.put(DatabaseContract.ArticleColums.POST_DATE, article.post_date)
                values.put(DatabaseContract.ArticleColums.CONTENT, article.content)
                values.put(DatabaseContract.ArticleColums.HAS_IMG, article.hasImg)
                values.put(DatabaseContract.ArticleColums.USTADZ_NAME, article.ustadzName)
                values.put(DatabaseContract.ArticleColums.IMGURL, article.imgUrl)
                dbArticleHelper.insert(values)
                downloaded = true
                fabDownload.setImageResource(R.drawable.ic_baseline_assignment_turned_in_24)
                Toast.makeText(this, "Artikel Berhasil Diunduh", Toast.LENGTH_LONG).show()
            }
            pullToRefresh.setOnRefreshListener { pullToRefresh.isRefreshing = false }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }
}

