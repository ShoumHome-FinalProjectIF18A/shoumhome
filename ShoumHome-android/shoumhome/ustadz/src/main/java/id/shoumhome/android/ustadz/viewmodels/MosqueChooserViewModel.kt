package id.shoumhome.android.ustadz.viewmodels

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
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.adapters.MosqueChooserAdapter
import id.shoumhome.android.ustadz.models.Mosque
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import org.json.JSONObject
import kotlin.collections.ArrayList

class MosqueChooserViewModel : ViewModel() {

    private var listMosques = MutableLiveData<ArrayList<Mosque>>()

    fun setMosque(context: Context, searchQuery: String = "", isUstadzOnly: Boolean = true): String? {
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }
        var ret: String? = null
        val credential = CredentialPreference(context)
        val endpoint = if (isUstadzOnly) "api/mosques/ustadz" else "api/mosques/user"
        val url = context.resources.getString(R.string.server) + endpoint
        val listItems = ArrayList<Mosque>()
        val client = SyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        if (isUstadzOnly) params.put("ustadz_id", credential.getCredential().username)
        params.put("q", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                for (i in 0 until list.length()) {
                    val mosqueJSONObject = list.getJSONObject(i)
                    val mosque = Mosque()

                    with(mosqueJSONObject) {
                        mosque.id = this.getInt("mosque_id")
                        mosque.mosqueName = this.getString("name")
                        mosque.address = this.getString("address")
                        mosque.latLng = this.getString("lat_lng")
                    }
                    listItems.add(mosque)
                    //adapter.notifyDataSetChanged()
                }
                listMosques.postValue(listItems)
                ret = null
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //Toast.makeText(context, "Failure!\n${error?.message}\n${responseBody?.let { String(it) }}\n${url}", Toast.LENGTH_SHORT).show()
                error?.printStackTrace()
                ret = "${responseBody?.let { String(it) }}"
            }
        })
        return ret
    }

    fun setMosqueAsync(context: Context,
                       adapter: MosqueChooserAdapter,
                       searchQuery: String = "",
                       isUstadzOnly: Boolean = true): Boolean {
        var ret = false
        val credential = CredentialPreference(context)
        val endpoint = if (isUstadzOnly) "api/mosques/ustadz" else "api/mosques/user"
        val url = context.resources.getString(R.string.server) + endpoint
        val listItems = ArrayList<Mosque>()
        val client = AsyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        if (isUstadzOnly) params.put("ustadz_id", credential.getCredential().username)
        params.put("q", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                for (i in 0 until list.length()) {
                    val mosqueJSONObject = list.getJSONObject(i)
                    val mosque = Mosque()

                    with(mosqueJSONObject) {
                        mosque.id = this.getInt("mosque_id")
                        mosque.mosqueName = this.getString("name")
                        mosque.address = this.getString("address")
                        mosque.latLng = this.getString("lat_lng")
                    }
                    listItems.add(mosque)
                    adapter.notifyDataSetChanged()
                }
                listMosques.postValue(listItems)
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

    fun getArticle(): LiveData<ArrayList<Mosque>> {
        return listMosques
    }

}