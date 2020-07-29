package id.shoumhome.android.ustadz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Credential (
        var username: String? = null,
        var password: String? = null
): Parcelable