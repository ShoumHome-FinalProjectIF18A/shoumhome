package id.shoumhome.android.data

import androidx.annotation.DrawableRes
import id.shoumhome.android.R

object DeveloperData {
    @DrawableRes
    val drawableId: IntArray = intArrayOf(
            R.drawable.dwichan,
            R.drawable.maratun,
            R.drawable.fendi
    )
    val name: Array<String?> = arrayOf(
            "Dwi Candra Permana",
            "Mar Atun Solehah",
            "Fendi Riawan"
    )
    val nim: Array<String?> = arrayOf(
            "18.11.0004",
            "18.11.0017",
            "18.11.0268"
    )
    val grade: Array<String?> = arrayOf(
            "IF 2018 A",
            "IF 2018 A",
            "IF 2018 A"
    )
    val yearRegistered: Array<String?> = arrayOf(
            "2018",
            "2018",
            "2018"
    )
    val githubUsername: Array<String?> = arrayOf(
            "dwichan0905",
            "maratun1725",
            "fendi-riawan"
    )
    val email: Array<String?> = arrayOf(
            "dwichan@outlook.com",
            "marhatunsoleha17@gmail.com",
            "fendiriawan0102@gmail.com"
    )
    val address: Array<String?> = arrayOf(
            "Jalan Mandor Martinem Roy 1 Nomor 99 RT 005 RW 006 Desa Rejasari Kecamatan Langensari Kota Banjar",
            "Losari RT 04 Rw 04 Kecamatan Rawalo Kabupaten Banyumas",
            "Kemutug Lor RT 04 RW 03 Kecamatan Baturraden"
    )
}