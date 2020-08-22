package id.shoumhome.android.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.R
import id.shoumhome.android.adapters.MosqueListAdapter
import id.shoumhome.android.viewmodels.MosqueListViewModel
import kotlinx.android.synthetic.main.fragment_mosque_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class MosqueListFragment : Fragment() {

    private var mosqueListAdapter = MosqueListAdapter()
    private lateinit var mosqueListViewModel: MosqueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mosqueListAdapter = MosqueListAdapter()
        mosqueListAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mosque_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mosqueListAdapter

        progressBar.visibility = View.VISIBLE

        mosqueListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MosqueListViewModel::class.java)
        mosqueListViewModel.getMosque().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mosqueListAdapter.setData(it)
                progressBar.visibility = View.GONE
            }
        })
        context?.let { mosqueListViewModel.setMosqueAsync(it, mosqueListAdapter, "") }

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredStatus = async(Dispatchers.IO) {
                    mosqueListViewModel.setMosque(requireActivity().applicationContext,
                            "")
                }
                val status = deferredStatus.await()
                if (status != null) {
                    pullToRefresh.isRefreshing = false
                    val parse = JSONObject(status)
                    Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    pullToRefresh.isRefreshing = false
                    mosqueListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_article_kajian_search, menu)

        // SearchView start
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = getString(R.string.search_mosque)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                mosqueListViewModel.setMosqueAsync(
                        requireContext(),
                        mosqueListAdapter,
                        query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                /* no-op */
                return true
            }
        })
        // SearchView end


        super.onCreateOptionsMenu(menu, inflater)
    }
}