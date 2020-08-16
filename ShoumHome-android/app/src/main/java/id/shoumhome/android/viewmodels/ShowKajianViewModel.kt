package id.shoumhome.android.viewmodels

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ShowKajianViewModel : ViewModel() {

    private val mData = MutableLiveData<Map<String, Any?>>()

    fun setKajian(context: Context, id: String): Map<String, Any?> {
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }
        val resultMap = mutableMapOf<String, Any?>()
        val url = context.resources.getString(R.string.server) + "api/kajian"
        val client = SyncHttpClient()

        // RequestParameter
        val params = RequestParams()
        params.put("read", "1")
        params.put("ustadz_mode", "0")
        params.put("kajian", id)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                val kajianJsonObject = list.getJSONObject(0)
                with (kajianJsonObject) {
                    val title = this.getString("kajian_title")
                    val ustadzName = this.getString("ustadz_name")
                    val mosqueName = this.getString("mosque_name")
                    val address = this.getString("address")
                    val category = this.getString("place")
                    val youtubeLink = this.getString("youtube_link")
                    val description = this.getString("description")
                    val imgResource = this.getString("img_resource")
                    val dateAnnounce = this.getString("date_announce")
                    val dateDue = this.getString("date_due")

                    // Convert String to Date, and back to String in different format!
                    val dateAnnounceD = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateAnnounce)!!
                    val dateDueD = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateDue)!!
                    val dateAnnounceFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateAnnounceD)
                    val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDueD)

                    resultMap["status"] = true
                    resultMap["id"] = id
                    resultMap["title"] = title
                    resultMap["ustadz_name"] = ustadzName
                    resultMap["mosque_name"] = mosqueName
                    resultMap["address"] = address
                    resultMap["category"] = category
                    resultMap["youtube_link"] = youtubeLink
                    resultMap["description"] = description
                    resultMap["img_resource"] = imgResource
                    resultMap["date_announce"] = dateAnnounceFormatted
                    resultMap["date_due"] = dateDueFormatted
                    mData.postValue(resultMap)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                resultMap["status"] = false
                resultMap["code"] = statusCode
                resultMap["message"] = error?.message + " id: $id"
                mData.postValue(resultMap)
            }
        })

        return resultMap
    }

    fun setKajianAsync(context: Context, id: String): Map<String, Any?> {
        val resultMap = mutableMapOf<String, Any?>()
        val url = context.resources.getString(R.string.server) + "api/kajian"
        val client = AsyncHttpClient()

        // RequestParameter
        val params = RequestParams()
        params.put("read", "1")
        params.put("ustadz_mode", "0")
        params.put("kajian", id)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                val kajianJsonObject = list.getJSONObject(0)
                with (kajianJsonObject) {
                    val title = this.getString("kajian_title")
                    val ustadzName = this.getString("ustadz_name")
                    val mosqueName = this.getString("mosque_name")
                    val address = this.getString("address")
                    val category = this.getString("place")
                    val youtubeLink = this.getString("youtube_link")
                    val description = this.getString("description")
                    val imgResource = this.getString("img_resource")
                    val dateAnnounce = this.getString("date_announce")
                    val dateDue = this.getString("date_due")

                    // Convert String to Date, and back to String in different format!
                    val dateAnnounceD = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateAnnounce)!!
                    val dateDueD = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateDue)!!
                    val dateAnnounceFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateAnnounceD)
                    val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDueD)

                    resultMap["status"] = true
                    resultMap["id"] = id
                    resultMap["title"] = title
                    resultMap["ustadz_name"] = ustadzName
                    resultMap["mosque_name"] = mosqueName
                    resultMap["address"] = address
                    resultMap["category"] = category
                    resultMap["youtube_link"] = youtubeLink
                    resultMap["description"] = description
                    resultMap["img_resource"] = imgResource
                    resultMap["date_announce"] = dateAnnounceFormatted
                    resultMap["date_due"] = dateDueFormatted
                    mData.postValue(resultMap)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                resultMap["status"] = false
                resultMap["code"] = statusCode
                resultMap["message"] = error?.message + " id: $id"
                mData.postValue(resultMap)
            }
        })

        return resultMap
    }

    fun getKajian(): LiveData<Map<String, Any?>> = mData
}