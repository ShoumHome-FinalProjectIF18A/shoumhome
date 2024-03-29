package id.shoumhome.android.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.CONTENT
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.HAS_IMG
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.ID
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.IMGURL
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.POST_DATE
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.TABLE_NAME_ARTICLES
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.TITLE
import id.shoumhome.android.databases.articles.DatabaseContract.ArticleColums.Companion.USTADZ_NAME
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.ADDRESS
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.DATE_ANNOUNCE
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.DATE_DUE
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.DESCRIPTION
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.IMG_RESOURCE
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.KAJIAN_TITLE
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.MOSQUE_NAME
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.PLACE
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.TABLE_NAME_KAJIAN
import id.shoumhome.android.databases.kajian.DatabaseContract.KajianColumns.Companion.YOUTUBE_LINK
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
            $POST_DATE Text,
            $CONTENT Text,
            $HAS_IMG Int,
            $USTADZ_NAME varchar(15),
            $IMGURL Text)
        """.trimIndent()

        private val SQL_CREATE_NAME_KAJIAN = """
            CREATE TABLE $TABLE_NAME_KAJIAN(
            $ID varchar(5) not null primary key,
            $KAJIAN_TITLE Text,
            $USTADZ_NAME varchar(15),
            $MOSQUE_NAME varchar(30),
            $ADDRESS Text,
            $PLACE varchar(30),
            $YOUTUBE_LINK Text,
            $DESCRIPTION Text,
            $IMG_RESOURCE Text,
            $DATE_ANNOUNCE Text,
            $DATE_DUE Text)
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_ARTIKEL)
        db?.execSQL(SQL_CREATE_NAME_KAJIAN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }



}
