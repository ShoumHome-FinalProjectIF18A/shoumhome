package id.shoumhome.android.viewmodels

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.R
import id.shoumhome.android.adapters.ListKajianAdapter
import id.shoumhome.android.models.Kajian
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class KajianViewModel : ViewModel() {

    private var listKajian = MutableLiveData<ArrayList<Kajian>>()

    fun setKajian(context: Context, searchQuery: String = ""): String? {
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }
        var ret: String? = null
        val url = context.resources.getString(R.string.server) + "api/kajian"
        val listItems = ArrayList<Kajian>()
        val client = SyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        params.put("read", "0")
        params.put("ustadz_mode", "1")
        params.put("ustadz_id", "admin") // replace admin with Ustadz ID
        params.put("q", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                for (i in 0 until list.length()) {
                    val kajianJSONObject = list.getJSONObject(i)
                    val kajian = Kajian()

                    with(kajianJSONObject) {
                        kajian.id = this.getString("id")
                        kajian.title = this.getString("kajian_title")
                        kajian.place = this.getString("place")
                        kajian.address = this.getString("address")
                        kajian.mosque = this.getString("mosque_name")

                        val date = this.getString("date_due")
                        val dateDue = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                        val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDue)

                        kajian.date = dateDueFormatted
                        kajian.imgResource = this.getString("img_resource")
                    }
                    listItems.add(kajian)
                    //adapter.notifyDataSetChanged()
                }
                listKajian.postValue(listItems)
                ret = null
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                error?.printStackTrace()
                ret = "${responseBody?.let { String(it) }}"
            }
        })
        return ret
    }

    fun setKajianAsync(context: Context, adapter: ListKajianAdapter, searchQuery: String = ""): Boolean {
        var ret = false
        val url = context.resources.getString(R.string.server) + "api/kajian"
        val listItems = ArrayList<Kajian>()
        val client = AsyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        params.put("read", "0")
        params.put("ustadz_mode", "1")
        params.put("ustadz_id", "admin") // replace admin with Ustadz ID
        params.put("q", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                for (i in 0 until list.length()) {
                    val kajianJSONObject = list.getJSONObject(i)
                    val kajian = Kajian()

                    with(kajianJSONObject) {
                        kajian.id = this.getString("id")
                        kajian.title = this.getString("kajian_title")
                        kajian.place = this.getString("place")
                        kajian.address = this.getString("address")
                        kajian.mosque = this.getString("mosque_name")

                        val date = this.getString("date_due")
                        val dateDue = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                        val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDue)

                        kajian.date = dateDueFormatted
                        kajian.imgResource = this.getString("img_resource")
                    }
                    listItems.add(kajian)
                    adapter.notifyDataSetChanged()
                }
                listKajian.postValue(listItems)
                ret = true
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Toast.makeText(context, "Failure!\n${error?.message}\n${responseBody?.let { String(it) }}\n${url}", Toast.LENGTH_SHORT).show()
                error?.printStackTrace()
                ret = false
            }
        })
        return ret
    }

    fun getKajian(): LiveData<ArrayList<Kajian>> {
        return listKajian
    }

}