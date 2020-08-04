package id.shoumhome.android.ustadz.fragments

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
import id.shoumhome.android.ustadz.AddUpdateKajianActivity
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.adapters.KajianAdapter
import id.shoumhome.android.ustadz.viewmodels.KajianViewModel
import kotlinx.android.synthetic.main.fragment_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

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
            GlobalScope.launch(Dispatchers.Main) {
                val deferredStatus = async(Dispatchers.IO) {
                    kajianViewModel.setKajian(requireActivity().applicationContext,
                            et_search_kajian.text.toString())
                }
                val status = deferredStatus.await()
                if (status != null) {
                    pullToRefresh.isRefreshing = false
                    val parse = JSONObject(status)
                    Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    pullToRefresh.isRefreshing = false
                    kajianAdapter.notifyDataSetChanged()
                }
            }
        }

        et_search_kajian.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progressMessage.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredStatus = async(Dispatchers.IO) {
                        kajianViewModel.setKajian(requireActivity().applicationContext,
                                et_search_kajian.text.toString())
                    }
                    val status = deferredStatus.await()
                    if (status != null) {
                        progressMessage.visibility = View.GONE
                        val parse = JSONObject(status)
                        Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
                    } else {
                        progressMessage.visibility = View.GONE
                        kajianAdapter.notifyDataSetChanged()
                    }
                }
            }

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
        kajianViewModel.setKajianAsync(requireActivity().applicationContext, kajianAdapter, "")

        fab_add_kajian.setOnClickListener {
            val i = Intent(activity?.applicationContext, AddUpdateKajianActivity::class.java)
            startActivityForResult(i, AddUpdateKajianActivity.RESULT_SAVE)
        }
    }

    private fun fetchKajian() {
        progressMessage.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            val deferredStatus = async(Dispatchers.IO) {
                kajianViewModel.setKajian(requireActivity().applicationContext,
                        et_search_kajian.text.toString())
            }
            val status = deferredStatus.await()
            if (status != null) {
                progressMessage.visibility = View.GONE
                val parse = JSONObject(status)
                Toast.makeText(context, parse.getString("message"), Toast.LENGTH_SHORT).show()
            } else {
                progressMessage.visibility = View.GONE
                kajianAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            AddUpdateKajianActivity.RESULT_SAVE -> fetchKajian()
        }
    }
}