package id.shoumhome.android.ustadz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReadArticleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
        const val EXTRA_ARTICLE_TITLE = "extra_article_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_article)
    }
}