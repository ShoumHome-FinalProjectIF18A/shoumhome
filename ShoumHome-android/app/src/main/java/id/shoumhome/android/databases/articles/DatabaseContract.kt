package id.shoumhome.android.databases.articles

import android.provider.BaseColumns

object DatabaseContract {
    class ArticleColums : BaseColumns {
        companion object {
            const val TABLE_NAME_ARTICLES = "articles"
            val ID = "id"
            val TITLE = "title"
            val POST_DATE = "post_data"
            val CONTENT = "content"
            val HAS_IMG = "has_img"
            val USTADZ_NAME = "ustadz_name"
        }

    }
}