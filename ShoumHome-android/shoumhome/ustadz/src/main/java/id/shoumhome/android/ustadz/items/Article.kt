package id.shoumhome.android.ustadz.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
        var id: String? = null,
        var title: String? = null,
        var content: String? = null,
        var post_date: String? = null,
        var hasImg: Boolean = false,
        var imgUrl: String? = null
) : Parcelable