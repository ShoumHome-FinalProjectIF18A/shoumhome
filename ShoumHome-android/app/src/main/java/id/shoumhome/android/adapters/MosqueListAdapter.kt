package id.shoumhome.android.adapters

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import id.shoumhome.android.R
import id.shoumhome.android.models.Mosque
import kotlinx.android.synthetic.main.item_mosque_list.view.*
import java.util.*
import kotlin.collections.ArrayList


class MosqueListAdapter : RecyclerView.Adapter<MosqueListAdapter.MosqueListViewHolder>() {

    private val mData = ArrayList<Mosque>()

    fun setData(mosque: ArrayList<Mosque>) {
        mData.clear()
        mData.addAll(mosque)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MosqueListViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mosque_list, parent, false)
        return MosqueListViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: MosqueListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class MosqueListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

                setOnClickListener {
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle(mosque.mosqueName)
                            .setMessage(resources.getString(R.string.choose_action_mosque))
                            .setPositiveButton(resources.getString(R.string.view_on_maps)) { _, _ ->
                                val gmmIntentUri = Uri.parse("geo:${mosque.latLng!!}?q=${mosque.latLng!!}(${mosque.mosqueName})")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                mapIntent.resolveActivity(context.packageManager)?.let {
                                    (context as Activity).startActivity(mapIntent)
                                }
                            }
                            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                                /* no-op */
                            }
                    val alertDialog = alert.create()
                    alertDialog.show()
                }
            }
        }
    }
}