package id.shoumhome.android.ustadz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kajian(
        var id: String? = null,
        var title: String? = null,
        var mosqueId: String? = null,
        var mosqueName: String? = null,
        var place: String? = null,
        var address: String? = null,
        var date: String? = null,
        var description: String? = null,
        var imgResource: String? = null,
        var ytLink: String? = null
) : Parcelable