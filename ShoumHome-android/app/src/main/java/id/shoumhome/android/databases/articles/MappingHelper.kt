package id.shoumhome.android.databases.articles

import android.database.Cursor
import id.shoumhome.android.models.Article


object MappingHelper {
    fun mapCursorToArrayList(articleCursor: Cursor?): ArrayList<Article>{
        var articles = ArrayList<Article>()
        articleCursor?.apply {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.TITLE))
                val post_date = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.POST_DATE))
                val content = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.CONTENT))
                val has_img = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.HAS_IMG))
                val ustadz_name = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.USTADZ_NAME))
                val article = Article()
                article.id = id
                article.title = title
                article.post_date = post_date
                article.content = content
                article.hasImg = has_img
                article.ustadzName = ustadz_name
                articles.add(article)
            }
        }

        return articles
    }

    fun mapCursorToObject(articleCursor: Cursor?): Article{
        var article = Article()
        articleCursor?.apply {
            moveToFirst()
            val id = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.ID))
            val title = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.TITLE))
            val post_date = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.POST_DATE))
            val content = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.CONTENT))
            val has_img = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.HAS_IMG))
            val ustadz_name = getString(getColumnIndexOrThrow(DatabaseContract.ArticleColums.USTADZ_NAME))

                article.id = id
                article.title = title
                article.post_date = post_date
                article.content = content
                article.hasImg = has_img
                article.ustadzName = ustadz_name
        }
        return article
    }
}