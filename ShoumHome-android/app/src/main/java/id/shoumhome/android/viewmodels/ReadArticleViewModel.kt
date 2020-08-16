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

class ReadArticleViewModel : ViewModel() {

    private val mData = MutableLiveData<Map<String, Any?>>()

    fun setArticle(context: Context, id: String): Map<String, Any?> {
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }
        val resultMap = mutableMapOf<String, Any?>()
        val url = context.resources.getString(R.string.server) + "api/articles"
        val client = SyncHttpClient()

        // RequestParameter
        val params = RequestParams()
        params.put("read", "1")
        params.put("ustadz_mode", "0")
        params.put("article", id)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")
                val like = resultObject.getString("likes")

                val articleJSONObject = list.getJSONObject(0)
                val title = articleJSONObject.getString("title")
                val hasImg = articleJSONObject.getString("has_img")!!.toBoolean()
                val extension = articleJSONObject.getString("extension")
                val content = articleJSONObject.getString("content")

                val date = articleJSONObject.getString("post_date")
                val datePost = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                val datePostFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(datePost)

                resultMap["status"] = true
                resultMap["id"] = id
                resultMap["title"] = title
                resultMap["hasImg"] = hasImg
                resultMap["extension"] = extension
                resultMap["content"] = content
                resultMap["post_date"] = datePostFormatted
                resultMap["like"] = like
                mData.postValue(resultMap)
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

    fun setArticleAsync(context: Context, id: String): Map<String, Any?> {
        val resultMap = mutableMapOf<String, Any?>()
        val url = context.resources.getString(R.string.server) + "api/articles"
        val client = AsyncHttpClient()

        // RequestParameter
        val params = RequestParams()
        params.put("read", "1")
        params.put("ustadz_mode", "0")
        params.put("article", id)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")
                val like = resultObject.getString("likes")

                val articleJSONObject = list.getJSONObject(0)
                val title = articleJSONObject.getString("title")
                val hasImg = articleJSONObject.getString("has_img")!!.toBoolean()
                val extension = articleJSONObject.getString("extension")
                val content = articleJSONObject.getString("content")

                val date = articleJSONObject.getString("post_date")
                val datePost = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                val datePostFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(datePost)

                resultMap["status"] = true
                resultMap["id"] = id
                resultMap["title"] = title
                resultMap["hasImg"] = hasImg
                resultMap["extension"] = extension
                resultMap["content"] = content
                resultMap["post_date"] = datePostFormatted
                resultMap["like"] = like
                mData.postValue(resultMap)
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

    fun getArticle(): LiveData<Map<String, Any?>> = mData
}