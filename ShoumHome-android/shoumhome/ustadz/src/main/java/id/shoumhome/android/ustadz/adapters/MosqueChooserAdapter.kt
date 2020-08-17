package id.shoumhome.android.ustadz.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import id.shoumhome.android.ustadz.MosqueChooserActivity
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.models.Mosque
import kotlinx.android.synthetic.main.item_mosque.view.*
import java.util.*
import kotlin.collections.ArrayList

class MosqueChooserAdapter : RecyclerView.Adapter<MosqueChooserAdapter.MosqueChooserViewHolder>() {

    private val mData = ArrayList<Mosque>()
    private lateinit var mContext: Context

    fun setData(mosque: ArrayList<Mosque>) {
        mData.clear()
        mData.addAll(mosque)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MosqueChooserAdapter.MosqueChooserViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_mosque, parent, false
        )
        return MosqueChooserViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: MosqueChooserAdapter.MosqueChooserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class MosqueChooserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mosque: Mosque) {
            with(itemView) {
                tvMosqueName.text = mosque.mosqueName
                if (mosque.address.isNullOrBlank()) {
                    val latLng = mosque.latLng!!
                    val latLngArray = latLng.split(",")
                    val latitude = latLngArray[0].toDouble()
                    val longitude = latLngArray[1].toDouble()
                    val location = LatLng(latitude, longitude)
                    val geocoder = Geocoder(itemView.context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1)
                    val address = addresses[0].getAddressLine(0)
                    val city = addresses[0].locality
                    val state = addresses[0].adminArea
                    val country = addresses[0].countryName
                    val postalCode = addresses[0].postalCode

                    val fullAddress = "$address, $city, $state, $country $postalCode"
                    tvMosqueAddress.text = fullAddress
                } else tvMosqueAddress.text = mosque.address
            }
            itemView.setOnClickListener {
                val i = Intent()
                i.putExtra(MosqueChooserActivity.EXTRA_MOSQUE_RESULT, mosque)
                with(mContext as Activity) {
                    setResult(MosqueChooserActivity.REQUEST_MOSQUE, i)
                    finish()
                }
            }
        }
    }
}