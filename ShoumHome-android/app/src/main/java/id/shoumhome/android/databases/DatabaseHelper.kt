package id.shoumhome.android.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.CONTENT
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.HAS_IMG
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.ID
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.POST_DATE
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.TABLE_NAME_ARTICLES
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.TITLE
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.USTADZ_NAME
import java.net.IDN

internal class DatabaseHelper (context: Context):
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "dbShoumHome"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_ARTIKEL = """
            CREATE TABLE $TABLE_NAME_ARTICLES(
            $ID varchar(5) not null primary key,
            $TITLE text,
            $POST_DATE String,
            $CONTENT String,
            $HAS_IMG Boolean,
            $USTADZ_NAME varchar(15))
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_ARTIKEL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

}
