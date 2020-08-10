package id.shoumhome.android.ustadz

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.ustadz.adapters.MosqueChooserAdapter
import id.shoumhome.android.ustadz.viewmodels.MosqueChooserViewModel
import kotlinx.android.synthetic.main.activity_mosque_chooser.*

class MosqueChooserActivity : AppCompatActivity() {

    private lateinit var mosqueChooserAdapter: MosqueChooserAdapter
    private lateinit var mosqueChooserViewModel: MosqueChooserViewModel
    private var isUstadzOnly = true

    companion object {
        const val EXTRA_MOSQUE_RESULT = "extra_mosque_result"
        const val EXTRA_IS_USTADZ_ONLY = "extra_is_ustadz_only"
        const val REQUEST_MOSQUE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // menghilangkan default title
        setContentView(R.layout.activity_mosque_chooser)
        progressMessage.visibility = View.VISIBLE

        val bundle = intent.extras!!
        isUstadzOnly = bundle.getBoolean(EXTRA_IS_USTADZ_ONLY, true)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.choose_mosque)

        // bind recyclerview adapter
        mosqueChooserAdapter = MosqueChooserAdapter()
        mosqueChooserAdapter.notifyDataSetChanged()

        rvMosqueList.layoutManager = LinearLayoutManager(this)
        rvMosqueList.adapter = mosqueChooserAdapter

        // penerapan mvvm
        mosqueChooserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MosqueChooserViewModel::class.java)
        mosqueChooserViewModel.getArticle().observe(this, Observer {
            // ini saat list berhasil di load
            if (it != null) {
                progressMessage.visibility = View.GONE
                mosqueChooserAdapter.setData(it)
            }
        })
        mosqueChooserViewModel.setMosqueAsync(
                this, mosqueChooserAdapter, isUstadzOnly = isUstadzOnly)

        btnBack.setOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_mosque_chooser, menu)

        // SearchView start
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_mosque)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressMessage.visibility = View.VISIBLE
                mosqueChooserViewModel.setMosqueAsync(
                        this@MosqueChooserActivity,
                        mosqueChooserAdapter,
                        query,
                        isUstadzOnly = isUstadzOnly)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                /* no-op */
                return true
            }
        })
        // SearchView end

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSave -> {

                true // wajib diakhiri ini!
            }
            else -> false
        }
    }
}