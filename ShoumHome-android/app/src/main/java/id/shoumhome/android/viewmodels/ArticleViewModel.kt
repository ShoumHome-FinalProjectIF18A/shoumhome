package id.shoumhome.android.viewmodels

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.ListAdapter
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
import id.shoumhome.android.models.Article
import id.shoumhome.android.adapters.ListArtikelAdapter

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArticleViewModel : ViewModel() {

    private var listArticle = MutableLiveData<ArrayList<Article>>()

    fun setArticle(context: Context, searchQuery: String = ""): String? {
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }
        var ret: String? = null
        val url = context.resources.getString(R.string.server) + "api/articles"
        val listItems = ArrayList<Article>()
        val client = SyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        params.put("read", "0")
        params.put("ustadz_mode", "1")
        params.put("query", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                Log.d("Isi", result)

                for (i in 0 until list.length()) {
                    val articleJSONObject = list.getJSONObject(i)
                    val article = Article()

                    with(articleJSONObject) {
                        article.id = this.getString("id")
                        article.title = this.getString("title")
                        article.content = this.getString("content")
                        article.ustadzName = this.getString("ustadz_name")
                        article.hasImg = this.getString("has_img")

                        if (article.hasImg == "1") {
                            val extension = this.getString("extension")
                            article.imgUrl = context.resources.getString(R.string.server) + "assets/articles/${article.id}.$extension"
                        }

                        val date = this.getString("post_date")
                        val dateDue = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                        val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDue)

                        article.post_date = dateDueFormatted
                    }
                    listItems.add(article)
                    //adapter.notifyDataSetChanged()
                }
                listArticle.postValue(listItems)
                ret = null
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                error?.printStackTrace()
                ret = "${responseBody?.let { String(it) }}"
            }
        })
        return ret
    }

    fun setArticleAsync(context: Context, adapter: ListArtikelAdapter, searchQuery: String = ""): Boolean {
        var ret = false
        val url = context.resources.getString(R.string.server) + "api/articles"
        val listItems = ArrayList<Article>()
        val client = AsyncHttpClient()

        // Request Parameters
        val params = RequestParams()
        params.put("read", "0")
        params.put("ustadz_mode", "1")
        params.put("ustadz_id", "admin") // replace admin with Ustadz ID
        params.put("query", searchQuery)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }!!
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("data")

                Log.d("Isi", result)

                for (i in 0 until list.length()) {
                    val articleJSONObject = list.getJSONObject(i)
                    val article = Article()

                    with(articleJSONObject) {
                        article.id = this.getString("id")
                        article.title = this.getString("title")
                        article.content = this.getString("content")
                        article.ustadzName = this.getString("ustadz_name")
                        article.hasImg = this.getString("has_img")

                        if (article.hasImg == "1") {
                            val extension = this.getString("extension")
                            article.imgUrl = context.resources.getString(R.string.server) + "assets/articles/${article.id}.$extension"
                        }

                        val date = this.getString("post_date")
                        val dateDue = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
                        val dateDueFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(dateDue)

                        article.post_date = dateDueFormatted
                    }
                    listItems.add(article)
                    adapter.notifyDataSetChanged()
                }
                listArticle.postValue(listItems)
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

    fun getArticle(): LiveData<ArrayList<Article>> {
        return listArticle
    }

}