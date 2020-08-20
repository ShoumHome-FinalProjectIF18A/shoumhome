package id.shoumhome.android.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.databases.kajian.DatabaseContract
import id.shoumhome.android.databases.kajian.DbKajianHelper
import id.shoumhome.android.databases.kajian.MappingHelper
import id.shoumhome.android.models.Kajian
import kotlinx.android.synthetic.main.activity_show_kajian.*
import kotlinx.android.synthetic.main.content_show_kajian.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowKajianSQLActivity:AppCompatActivity() {
    private lateinit var kajian: Kajian
    private var remindered = false
    private lateinit var dbKajianHelper: DbKajianHelper

    companion object{
        const val EXTRA_PARCEL_KAJIAN = "extra_parcel_kajian"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_kajian)
        progressMessage.visibility = View.GONE
        errorMessage.visibility = View.GONE
        val bundle = intent.extras!!
        kajian = bundle?.getParcelable<Kajian>(EXTRA_PARCEL_KAJIAN)!!
        tvKajianTitle.text = kajian.title
        tvMosqueAddress.text = kajian.mosque
        tvUstadzName.text = kajian.ustadzName
        tvCategory.text = kajian.place
        tvTimestampAnnounce.text = kajian.date
        tvTimestampDue.text = kajian.dateAnnounce
        tvDescription.text = kajian.description
        Glide.with(this)
                .load(kajian.imgResource)
                .into(imgThumbnail)

        dbKajianHelper = DbKajianHelper.getInstance(this)
        dbKajianHelper.open()
        GlobalScope.launch(Dispatchers.Main) {
            val deferredKajian = async(Dispatchers.IO){
                val cursor = kajian.id?.let { dbKajianHelper.queryById(it) }
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val isReminder  = deferredKajian.await()
            if (isReminder.size == 0){
                remindered = false
                fabreminder.setImageResource(R.drawable.ic_baseline_notifications_none_24)
            }else{
                remindered = true
                fabreminder.setImageResource(R.drawable.ic_baseline_notifications_active_24)
            }
        }

        fabreminder.setOnClickListener{
            if (remindered){
                kajian.id?.let { it1 -> dbKajianHelper.deleteById(it1) }
                remindered = false
                fabreminder.setImageResource(R.drawable.ic_baseline_notifications_none_24)
                Toast.makeText(this, "Pengingat Telah di NonAtifkan", Toast.LENGTH_LONG).show()
            }else{
                val values = ContentValues()
                values.put(DatabaseContract.KajianColumns.ID, kajian.id)
                values.put(DatabaseContract.KajianColumns.KAJIAN_TITLE, kajian.title)
                values.put(DatabaseContract.KajianColumns.USTADZ_NAME, kajian.ustadzName)
                values.put(DatabaseContract.KajianColumns.MOSQUE_NAME, kajian.mosque)
                values.put(DatabaseContract.KajianColumns.ADDRESS, kajian.address)
                values.put(DatabaseContract.KajianColumns.PLACE, kajian.place)
                values.put(DatabaseContract.KajianColumns.YOUTUBE_LINK, kajian.youtubelink)
                values.put(DatabaseContract.KajianColumns.DESCRIPTION, kajian.description)
                values.put(DatabaseContract.KajianColumns.IMG_RESOURCE, kajian.imgResource)
                values.put(DatabaseContract.KajianColumns.DATE_ANNOUNCE, kajian.dateAnnounce)
                values.put(DatabaseContract.KajianColumns.DATE_DUE, kajian.date)
                dbKajianHelper.insert(values)
                remindered = true
                fabreminder.setImageResource(R.drawable.ic_baseline_notifications_active_24)
                Toast.makeText(this, "Pengingat Telah di Aktifkan", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> super.onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}
