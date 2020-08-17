package id.shoumhome.android.ustadz.adapters

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.models.Mosque
import id.shoumhome.android.ustadz.preferences.CredentialPreference
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
        holder.bind(mData[position], position)
    }

    inner class MosqueListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mosque: Mosque, position: Int) {
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
                            .setPositiveButton(resources.getString(R.string.view_on_maps)) { dialogInterface, i ->
                                val gmmIntentUri = Uri.parse("geo:${mosque.latLng!!}?q=${mosque.latLng!!}(${mosque.mosqueName})")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                mapIntent.resolveActivity(context.packageManager)?.let {
                                    (context as Activity).startActivity(mapIntent)
                                }
                            }
                            .setNegativeButton(resources.getString(R.string.delete)) { dialogInterface, i ->
                                val alertDelete = AlertDialog.Builder(context)
                                alertDelete.setTitle(resources.getString(R.string.sure))
                                        .setMessage(resources.getString(R.string.delete_mosque_confirm) + mosque.mosqueName + "?")
                                        .setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, i ->
                                            val credential = CredentialPreference(context)
                                            val ustadzId = credential.getCredential().username
                                            val api = resources.getString(R.string.server) + "api/mosques/${mosque.id}/$ustadzId"
                                            val client = AsyncHttpClient()
                                            client.delete(api, object : AsyncHttpResponseHandler() {
                                                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                                                    mData.removeAt(position)
                                                    notifyDataSetChanged()
                                                    Toast.makeText(context, resources.getString(R.string.delete_mosque_success) + mosque.mosqueName + "!", Toast.LENGTH_SHORT).show()
                                                }

                                                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                                                    Toast.makeText(context, resources.getString(R.string.delete_mosque_failed) + mosque.mosqueName + "!", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                        .setNegativeButton(resources.getString(R.string.no)) { dialogInterface, i ->
                                            /* no-op */
                                        }

                                val alertDialogDelete = alertDelete.create()
                                alertDialogDelete.show()
                            }
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialogInterface, i ->
                                /* no-op */
                            }
                    val alertDialog = alert.create()
                    alertDialog.show()
                }
            }
        }
    }
}