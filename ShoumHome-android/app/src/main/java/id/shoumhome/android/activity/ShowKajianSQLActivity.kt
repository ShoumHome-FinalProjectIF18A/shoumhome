package id.shoumhome.android.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.models.Kajian
import kotlinx.android.synthetic.main.content_show_kajian.*

class ShowKajianSQLActivity:AppCompatActivity() {
    private lateinit var kajian: Kajian
    companion object{
        const val EXTRA_PARCEL_KAJIAN = "extra_parcel_kajian"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_kajian)
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
    }
}