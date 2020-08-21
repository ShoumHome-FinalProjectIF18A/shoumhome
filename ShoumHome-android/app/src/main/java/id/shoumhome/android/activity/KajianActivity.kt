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
import id.shoumhome.android.adapters.ListKajianAdapter
import id.shoumhome.android.viewmodels.KajianViewModel
import kotlinx.android.synthetic.main.activity_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class KajianActivity : AppCompatActivity() {
    var id = ""
    private var kajianViewModel: KajianViewModel? = null
    private var listKajianAdapter = ListKajianAdapter(this)
    private lateinit var searchView: SearchView

    //buat di ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kajian)

        progressBar.visibility = View.VISIBLE

        val tb = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.show_kajian)

        recDataKajian.layoutManager = LinearLayoutManager(this)
        recDataKajian.adapter = listKajianAdapter

        kajianViewModel = ViewModelProvider(this, NewInstanceFactory())
                .get(KajianViewModel::class.java)
        kajianViewModel!!.getKajian().observe(this, Observer{
            if (it != null) {
                progressBar.visibility = View.GONE
                listKajianAdapter.setKajian(it)
            }
        })
        kajianViewModel!!.setKajianAsync(this, listKajianAdapter, "")

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch (Dispatchers.Main) {
                val deferredKajian = async (Dispatchers.IO) {
                    kajianViewModel!!.setKajian(this@KajianActivity, searchView.query.toString())
                }
                val status = deferredKajian.await()
                if (status != null) {
                    val parse = JSONObject(status)
                    Toast.makeText(this@KajianActivity, parse.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    listKajianAdapter.notifyDataSetChanged()
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
        searchView.queryHint = getString(R.string.search_kajian)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                GlobalScope.launch (Dispatchers.Main) {
                    val deferredKajian = async (Dispatchers.IO) {
                        kajianViewModel!!.setKajian(this@KajianActivity, query)
                    }
                    val status = deferredKajian.await()
                    if (status != null) {
                        val parse = JSONObject(status)
                        Toast.makeText(this@KajianActivity, parse.getString("message"), Toast.LENGTH_SHORT).show()
                    } else {
                        listKajianAdapter.notifyDataSetChanged()
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