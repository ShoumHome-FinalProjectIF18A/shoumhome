package id.shoumhome.android.ustadz.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.MosqueChooserActivity
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.adapters.MosqueListAdapter
import id.shoumhome.android.ustadz.models.Mosque
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import id.shoumhome.android.ustadz.viewmodels.MosqueListViewModel
import kotlinx.android.synthetic.main.fragment_mosque_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class MosqueListFragment : Fragment() {

    private var mosqueListAdapter: MosqueListAdapter = MosqueListAdapter()
    private lateinit var mosqueListViewModel: MosqueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        rvMosque.layoutManager = LinearLayoutManager(context)
        rvMosque.adapter = mosqueListAdapter

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredStatus = async(Dispatchers.IO) {
                    mosqueListViewModel.setMosque(requireActivity().applicationContext,
                            et_search_mosque.text.toString())
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

        et_search_mosque.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progressMessage.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredStatus = async(Dispatchers.IO) {
                        mosqueListViewModel.setMosque(requireActivity().applicationContext,
                                et_search_mosque.text.toString())
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
            true
        }

        mosqueListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MosqueListViewModel::class.java)
        mosqueListViewModel.getMosque().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mosqueListAdapter.setData(it)
                progressMessage.visibility = View.GONE
            }
        })
        context?.let { mosqueListViewModel.setMosqueAsync(it, mosqueListAdapter) }

        fab_add_mosque.setOnClickListener {
            val i = Intent(context, MosqueChooserActivity::class.java)
            i.putExtra(MosqueChooserActivity.EXTRA_IS_USTADZ_ONLY, false)
            startActivityForResult(i, MosqueChooserActivity.REQUEST_MOSQUE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            MosqueChooserActivity.REQUEST_MOSQUE -> {
                if (data != null) {
                    val credential = CredentialPreference(requireActivity().applicationContext!!)

                    val mosque = data.getParcelableExtra<Mosque>(MosqueChooserActivity.EXTRA_MOSQUE_RESULT)!!
                    val progressMsg = resources.getString(R.string.progress_add_mosque) + mosque.mosqueName
                    progressMessage.visibility = View.VISIBLE
                    tvProgressMessage.text = progressMsg

                    val mosqueId = mosque.id
                    val ustadzId = credential.getCredential().username

                    val api = resources.getString(R.string.server) + "api/mosques"
                    val client = AsyncHttpClient()

                    val params = RequestParams()
                    params.put("id", mosqueId)
                    params.put("ustadz_id", ustadzId)

                    client.post(api, params, object: AsyncHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                            Toast.makeText(context, resources.getString(R.string.add_mosque_success) + mosque.mosqueName, Toast.LENGTH_SHORT).show()
                            progressMessage.visibility = View.VISIBLE
                            GlobalScope.launch(Dispatchers.Main) {
                                val deferredStatus = async(Dispatchers.IO) {
                                    mosqueListViewModel.setMosque(requireActivity().applicationContext,
                                            et_search_mosque.text.toString())
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

                        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                            Toast.makeText(context, resources.getString(R.string.add_mosque_failed) + mosque.mosqueName, Toast.LENGTH_SHORT).show()
                            progressMessage.visibility = View.GONE
                        }

                    })
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}