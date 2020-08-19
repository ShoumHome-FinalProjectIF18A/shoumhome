package id.shoumhome.android.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.models.Article
import kotlinx.android.synthetic.main.content_read_article.*

class ReadArticleSQLActivity: AppCompatActivity(){
    private lateinit var article: Article
    companion object{
        const val EXTRA_PARCEL_ARTICLES = "extra_parcel_articles"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_article)

        val bundle = intent.extras!!
        article = bundle?.getParcelable<Article>(EXTRA_PARCEL_ARTICLES)!!
        tvArticleTitle.text = article.title
        tvPostDate.text = article.post_date
        tv_nama_Ustad.text = article.ustadzName
        tvPostContent.text = article.content
                Glide.with(this)
                        .load(article.imgUrl)
                        .into(imgArticle)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_artikel, menu)
        return super.onCreateOptionsMenu(menu)
    }

}