package id.shoumhome.android.activityHandler

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import id.shoumhome.android.R

class IntroActivity : AppIntro2() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make sure you don't call setContentView!
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlides()
        // Ask for required permission
        askForPermissions(
                permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                slideNumber = 1, // first slide
                required = true)

        /* APPEARANCES */
        // Hide/Show the status Bar
        showStatusBar(true)

        // Wizard Mode
        isWizardMode = true

        // You can customize your parallax parameters in the constructors.
        setTransformer(AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
        ))

        // Toggle Indicator Visibility
        isIndicatorEnabled = true
        // Change Indicator Color
        setIndicatorColor(
                selectedIndicatorColor = getColor(R.color.biru),
                unselectedIndicatorColor = getColor(R.color.abuabu)
        )
    }

    private fun addSlides(): Unit {
        addSlide(AppIntroFragment.newInstance(
                title = "Tetap menemanimu!",
                description = """
                    Ramadhan mu kurang berwarna karena larangan untuk keluar, sakit, atau sedang dalam perjalanan? Tenang, ShoumHome akan menemanimu dengan artikel dan informasi kajian online dari ustadz/ustadzah di sekitarmu!
                """.trimIndent(),
                titleColor = Color.BLACK,
                imageDrawable = R.drawable.intro1,
                descriptionColor = Color.BLACK,
                backgroundColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Beli takjil? Gak perlu bingung lagi!",
                description = """
                    Sekarang udah gak perlu bingung cari takjil kemana, beli saja secara online dari para pedagang takjil disekitarmu!
                """.trimIndent(),
                titleColor = Color.BLACK,
                imageDrawable = R.drawable.intro2,
                descriptionColor = Color.BLACK,
                backgroundColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Kajian, Jadwal Shalat, dan lain-lain!",
                description = """
                    Ingin mendapatkan info kajian, jadwal shalat, jadwal imsakiyah, atau takut kesiangan saat waktu sahur tiba? Semuanya ada dalam genggamanmu! Kita bakal ngasih tau kapan semuanya telah tiba!
                """.trimIndent(),
                titleColor = Color.BLACK,
                imageDrawable = R.drawable.intro3,
                descriptionColor = Color.BLACK,
                backgroundColor = Color.WHITE
        ))
    }

    override fun onUserDeniedPermission(permissionName: String) {
        // User pressed "Deny" on the permission dialog
        Toast.makeText(applicationContext, "Maaf, Anda harus memberikan izin aplikasi ini untuk membaca lokasi!", Toast.LENGTH_SHORT).show()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        with (sp.edit()) {
            putBoolean("first", true)
            commit()
        }
        val home = Intent(this@IntroActivity, LayarUtama::class.java)
        startActivity(home)
        finish()
    }
}
