package id.shoumhome.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mosque(
        var id: Int = 0,
        var mosqueName: String? = null,
        var latLng: String? = null,
        var address: String? = null
) : Parcelable