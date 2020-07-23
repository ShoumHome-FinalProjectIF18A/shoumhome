package id.shoumhome.android.ustadz.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kajian (
        var id: String? = null,
        var title: String? = null,
        var mosque: String? = null,
        var place: String? = null,
        var address: String? = null,
        var date: String? = null,
        var imgResource: String? = null
): Parcelable