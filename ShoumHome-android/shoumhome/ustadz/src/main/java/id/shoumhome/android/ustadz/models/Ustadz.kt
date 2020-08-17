package id.shoumhome.android.ustadz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ustadz(
        var id: String? = null,
        var name: String? = null,
        var phone: String? = null,
        var address: String? = null,
        var email: String? = null,
        var gender: Char? = null
) : Parcelable