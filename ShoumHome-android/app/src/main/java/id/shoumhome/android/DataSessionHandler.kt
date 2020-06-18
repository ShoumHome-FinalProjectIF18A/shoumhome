package id.shoumhome.android

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataSessionHandler(
        var username: String = "",
        var password: String = "",
        var nama_lengkap: String = "",
        var email: String = "") : Parcelable {
}