package id.shoumhome.android.ustadz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.adapters.ArticleAdapter
import id.shoumhome.android.ustadz.viewmodels.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class ArticlesFragment : Fragment() {

    private var articleAdapter = ArticleAdapter()
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleAdapter = ArticleAdapter()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_articles.layoutManager = LinearLayoutManager(context)
        rv_articles.adapter = articleAdapter

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredStatus = async(Dispatchers.IO) {
                    articleViewModel.setArticle(requireActivity().applicationContext,
                            et_search_article.text.toString())
                }
                val status = deferredStatus.await()
                if (status != null) {
                    pullToRefresh.isRefreshing = false
                    val parse = JSONObject(status)
                    Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    pullToRefresh.isRefreshing = false
                    articleAdapter.notifyDataSetChanged()
                }
            }
        }

        et_search_article.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progressMessage.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredStatus = async(Dispatchers.IO) {
                        articleViewModel.setArticle(requireActivity().applicationContext,
                                et_search_article.text.toString())
                    }
                    val status = deferredStatus.await()
                    if (status != null) {
                        progressMessage.visibility = View.GONE
                        val parse = JSONObject(status)
                        Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
                    } else {
                        progressMessage.visibility = View.GONE
                        articleAdapter.notifyDataSetChanged()
                    }
                }
            }

            true
        }

        articleViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(ArticleViewModel::class.java)
        articleViewModel.getArticle().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                articleAdapter.setData(it)
                progressMessage.visibility = View.GONE
            }
        })
        articleViewModel.setArticleAsync(requireActivity(), articleAdapter, "")
    }
}