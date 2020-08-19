package id.shoumhome.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.R
import id.shoumhome.android.adapters.ArticleSQLAdapter
import id.shoumhome.android.databases.articles.DbArticleHelper
import id.shoumhome.android.databases.articles.MappingHelper
import kotlinx.android.synthetic.main.fragment_downloaded_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DownloadedArticleFragment : Fragment() {
    private lateinit var articleSQLAdapter: ArticleSQLAdapter
    private lateinit var dbArticleHelper: DbArticleHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_articles.layoutManager = LinearLayoutManager(context)
        rv_articles.adapter = articleSQLAdapter
        rv_articles.setHasFixedSize(true)//ukuran tidak aka berubah jk diputar

        //manggil database(buka)
        dbArticleHelper = DbArticleHelper.getInstance(requireContext().applicationContext)
        dbArticleHelper.open()

        loadArticle()
    }

    private fun loadArticle() {
        GlobalScope.launch(Dispatchers.Main){
            val deferredArticle = async (Dispatchers.IO){
                val cursor = dbArticleHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val article = deferredArticle.await()
            if(article.size > 0) articleSQLAdapter.setArticle(article)
            else{
                articleSQLAdapter.setArticle(ArrayList())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleSQLAdapter = ArticleSQLAdapter()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_downloaded_article, container, false)
    }
}