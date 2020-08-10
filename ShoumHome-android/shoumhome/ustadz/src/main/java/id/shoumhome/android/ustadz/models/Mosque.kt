package id.shoumhome.android.ustadz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mosque (
        var id: Int = 0,
        var mosqueName: String? = null,
        var latLng: String? = null,
        var address: String? = null,
        var isFilteredByUstadz: Boolean ?= false
): Parcelable