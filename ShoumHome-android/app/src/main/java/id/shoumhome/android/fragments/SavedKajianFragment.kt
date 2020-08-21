package id.shoumhome.android.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.shoumhome.android.R
import id.shoumhome.android.activity.ShowKajianSQLActivity
import id.shoumhome.android.adapters.KajianSQLAdapter
import id.shoumhome.android.databases.kajian.DbKajianHelper
import id.shoumhome.android.databases.kajian.MappingHelper
import kotlinx.android.synthetic.main.fragment_saved_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SavedKajianFragment : Fragment() {
    private lateinit var kajianSQLAdapter: KajianSQLAdapter
    private lateinit var dbKajianHelper: DbKajianHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_kajian.layoutManager = LinearLayoutManager(context)
        rv_kajian.adapter = kajianSQLAdapter
        rv_kajian.setHasFixedSize(true) //tidak efek ketika diputar

        //memanggil database
        dbKajianHelper = DbKajianHelper.getInstance(requireContext().applicationContext)
        dbKajianHelper.open()

        loadKajian()
    }

    private fun loadKajian() {
        GlobalScope.launch ( Dispatchers.Main ) {
            val deferredKajian = async (Dispatchers.IO){
                val cursor = dbKajianHelper.queryAll()

                MappingHelper.mapCursorToArrayList(cursor)
            }
            val kajian = deferredKajian.await()
            if (kajian.size >0)kajianSQLAdapter.setData(kajian)
                else{
                    kajianSQLAdapter.setData(ArrayList())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kajianSQLAdapter = KajianSQLAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_kajian, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadKajian()
    }

}