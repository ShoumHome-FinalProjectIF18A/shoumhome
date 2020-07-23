package id.shoumhome.android.ustadz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.adapters.KajianAdapter
import id.shoumhome.android.ustadz.viewmodels.KajianViewModel
import kotlinx.android.synthetic.main.fragment_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class KajianFragment : Fragment() {

    private var kajianAdapter = KajianAdapter()
    private lateinit var kajianViewModel: KajianViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kajianAdapter = KajianAdapter()
        kajianAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kajian, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview adapter
        rv_info.layoutManager = LinearLayoutManager(context)
        rv_info.adapter = kajianAdapter

        pullToRefresh.setOnRefreshListener {
            GlobalScope.launch (Dispatchers.Main) {
                val deferredStatus = async (Dispatchers.IO) {
                    kajianViewModel.setUsers(requireActivity().applicationContext,
                            et_search_kajian.text.toString())
                }
                val status = deferredStatus.await()
                if (!status) {
                    pullToRefresh.isRefreshing = false
                } else {
                    pullToRefresh.isRefreshing = false
                    kajianAdapter.notifyDataSetChanged()
                }
            }
        }

        et_search_kajian.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
                kajianViewModel.setUsersAsync(requireActivity().applicationContext,
                        kajianAdapter,
                        et_search_kajian.text.toString())

            true
        }

        kajianViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(KajianViewModel::class.java)
        kajianViewModel.getKajian().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                kajianAdapter.setData(it)
                progressMessage.visibility = View.GONE
            }
        })
        kajianViewModel.setUsersAsync(requireActivity().applicationContext, kajianAdapter,"")
    }
}