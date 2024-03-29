package id.shoumhome.android.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.R
import id.shoumhome.android.adapters.ListArtikelAdapter
import id.shoumhome.android.viewmodels.ArticleViewModel
import kotlinx.android.synthetic.main.activity_artikel__islami.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class ArtikelActivity : AppCompatActivity() {
    var id = ""
    private var articleViewModel: ArticleViewModel? = null
    private var listArtikelAdapter = ListArtikelAdapter(this)
    private lateinit var searchView: SearchView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }

    override fun onCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        setContentView(R.layout.activity_artikel__islami)

        progressBar.visibility = View.VISIBLE

        // fungsi Toolbar dan ActionBar(back pd artikel)
        val tb = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.read_article)

        recDataArtikel.layoutManager = LinearLayoutManager(this)
        recDataArtikel.adapter = listArtikelAdapter
        articleViewModel = ViewModelProvider(this, NewInstanceFactory())
                .get(ArticleViewModel::class.java)
        articleViewModel!!.getArticle().observe(this, Observer {
            if (it != null) {
                progressBar.visibility = View.GONE
                listArtikelAdapter.setArticle(it)
            }
        })
        articleViewModel!!.setArticleAsync(this, listArtikelAdapter, "")

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch (Dispatchers.Main) {
                val deferredArticles = async(Dispatchers.IO) {
                    articleViewModel!!.setArticle(this@ArtikelActivity, searchView.query.toString())
                }
                val status = deferredArticles.await()
                if (status != null) {
                    val parse = JSONObject(status)
                    Toast.makeText(this@ArtikelActivity, parse.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    listArtikelAdapter.notifyDataSetChanged()
                }
                pullToRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_kajian_search, menu)

        // SearchView start
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_article)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                GlobalScope.launch (Dispatchers.Main) {
                    val deferredArticles = async(Dispatchers.IO) {
                        articleViewModel!!.setArticle(this@ArtikelActivity, searchView.query.toString())
                    }
                    val status = deferredArticles.await()
                    if (status != null) {
                        val parse = JSONObject(status)
                        Toast.makeText(this@ArtikelActivity, parse.getString("message"), Toast.LENGTH_SHORT).show()
                    } else {
                        listArtikelAdapter.notifyDataSetChanged()
                    }
                    progressBar.visibility = View.GONE
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                /* no-op */
                return true
            }
        })
        return true
    }
}