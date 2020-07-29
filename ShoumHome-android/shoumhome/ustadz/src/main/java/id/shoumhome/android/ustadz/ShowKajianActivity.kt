package id.shoumhome.android.ustadz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.shoumhome.android.ustadz.viewmodels.ShowKajianViewModel
import kotlinx.android.synthetic.main.activity_show_kajian.*
import kotlinx.android.synthetic.main.content_show_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ShowKajianActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var showKajianViewModel: ShowKajianViewModel
    private var id: String = ""

    companion object {
        const val EXTRA_KAJIAN_ID = "extra_kajian_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_kajian)
        progressMessage.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE

        // Ambil Bundle Value
        val bundle = intent.extras
        id = bundle?.get(EXTRA_KAJIAN_ID).toString()

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.view_kajian)

        // MVVM Show Kajian
        showKajianViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(ShowKajianViewModel::class.java)
        showKajianViewModel.getKajian().observe(this, Observer {
            if (it["status"] == true) {
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                val title = it["title"].toString()
                val ustadzName = resources.getString(R.string.by) + " " + it["ustadz_name"].toString()
                val mosqueName = it["mosque_name"].toString()
                val address = it["address"].toString()
                val description = it["description"].toString()
                val dateAnnounce = resources.getString(R.string.timestamp_announce) + " " + it["date_announce"].toString()
                val dateDue = resources.getString(R.string.timestamp_due) + " " + it["date_due"]

                tvKajianTitle.text = title
                tvUstadzName.text = ustadzName
                tvDescription.text = description
                tvTimestampAnnounce.text = dateAnnounce
                tvTimestampDue.text = dateDue

                if (it["category"] == "Di Tempat") {
                    btnPlay.visibility = View.GONE
                    tvCategory.text = it["category"].toString()
                    tvMosqueAddress.text = address
                } else {
                    btnPlay.visibility = View.VISIBLE
                    val category = it["category"].toString().toUpperCase(Locale.ROOT) + " - Courtesy of YouTube"
                    tvCategory.text = category
                    tvMosqueAddress.text = mosqueName
                    val uri = it["youtube_link"].toString()
                    btnPlay.setOnClickListener {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(i)
                    }
                }

                if (it["img_resource"] != null) {
                    Glide.with(this)
                            .load(it["img_resource"].toString())
                            .into(imgThumbnail)
                } else {
                    imgThumbnail.visibility = View.GONE
                    btnPlay.visibility = View.GONE
                }
            } else {
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
                val errorMessage = """
                    Error!
                    [${it["code"]}]: ${it["message"]}
                """.trimIndent()
                tvErrorMessage.text = errorMessage
            }
        })
        showKajianViewModel.setKajianAsync(this, id)

        // Pull-to-Refresh
        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
            progressMessage.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            GlobalScope.launch (Dispatchers.Main) {
                val deferredKajian = async (Dispatchers.IO) {
                    showKajianViewModel.setKajian(applicationContext, id)
                }
                val it = deferredKajian.await()
                if (it["status"] == true) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    val title = it["title"].toString()
                    val ustadzName = resources.getString(R.string.by) + " " + it["ustadz_name"].toString()
                    val mosqueName = it["mosque_name"].toString()
                    val address = it["address"].toString()
                    val description = it["description"].toString()
                    val dateAnnounce = resources.getString(R.string.timestamp_announce) + " " + it["date_announce"].toString()
                    val dateDue = resources.getString(R.string.timestamp_due) + " " + it["date_due"]

                    tvKajianTitle.text = title
                    tvUstadzName.text = ustadzName
                    tvDescription.text = description
                    tvTimestampAnnounce.text = dateAnnounce
                    tvTimestampDue.text = dateDue

                    if (it["category"] == "Di Tempat") {
                        btnPlay.visibility = View.GONE
                        tvCategory.text = it["category"].toString()
                        tvMosqueAddress.text = address
                    } else {
                        btnPlay.visibility = View.VISIBLE
                        val category = it["category"].toString().toUpperCase(Locale.ROOT) + " - Courtesy of YouTube"
                        tvCategory.text = category
                        tvMosqueAddress.text = mosqueName
                        val uri = it["youtube_link"].toString()
                        btnPlay.setOnClickListener {
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            startActivity(i)
                        }
                    }

                    if (it["img_resource"] != null) {
                        Glide.with(applicationContext)
                                .load(it["img_resource"].toString())
                                .into(imgThumbnail)
                    } else {
                        imgThumbnail.visibility = View.GONE
                        btnPlay.visibility = View.GONE
                    }
                } else {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMessage = """
                        Error!
                        [${it["code"]}]: ${it["message"]}
                    """.trimIndent()
                    tvErrorMessage.text = errorMessage
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnRefresh -> {
                // saat tombol Refresh ditekan
                pullToRefresh.isRefreshing = false
                progressMessage.visibility = View.VISIBLE
                errorMessage.visibility = View.GONE
                GlobalScope.launch (Dispatchers.Main) {
                    val deferredKajian = async (Dispatchers.IO) {
                        showKajianViewModel.setKajian(applicationContext, id)
                    }
                    val it = deferredKajian.await()
                    if (it["status"] == true) {
                        progressMessage.visibility = View.GONE
                        errorMessage.visibility = View.GONE
                        val title = it["title"].toString()
                        val ustadzName = resources.getString(R.string.by) + " " + it["ustadz_name"].toString()
                        val mosqueName = it["mosque_name"].toString()
                        val address = it["address"].toString()
                        val description = it["description"].toString()
                        val dateAnnounce = resources.getString(R.string.timestamp_announce) + " " + it["date_announce"].toString()
                        val dateDue = resources.getString(R.string.timestamp_due) + " " + it["date_due"]

                        tvKajianTitle.text = title
                        tvUstadzName.text = ustadzName
                        tvDescription.text = description
                        tvTimestampAnnounce.text = dateAnnounce
                        tvTimestampDue.text = dateDue

                        if (it["category"] == "Di Tempat") {
                            btnPlay.visibility = View.GONE
                            tvCategory.text = it["category"].toString()
                            tvMosqueAddress.text = address
                        } else {
                            btnPlay.visibility = View.VISIBLE
                            val category = it["category"].toString().toUpperCase(Locale.ROOT) + " - Courtesy of YouTube"
                            tvCategory.text = category
                            tvMosqueAddress.text = mosqueName
                            val uri = it["youtube_link"].toString()
                            btnPlay.setOnClickListener {
                                val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                startActivity(i)
                            }
                        }

                        if (it["img_resource"].toString() != "null") {
                            Glide.with(applicationContext)
                                    .load(it["img_resource"].toString())
                                    .into(imgThumbnail)
                        } else {
                            imgThumbnail.visibility = View.GONE
                            btnPlay.visibility = View.GONE
                        }
                    } else {
                        progressMessage.visibility = View.GONE
                        errorMessage.visibility = View.VISIBLE
                        val errorMessage = """
                            Error!
                            [${it["code"]}]: ${it["message"]}
                        """.trimIndent()
                        tvErrorMessage.text = errorMessage
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_kajian, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }
}