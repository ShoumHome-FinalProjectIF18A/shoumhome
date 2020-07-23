package id.shoumhome.android.ustadz

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

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
                title = "Jalin hubungan bersama masyarakat",
                description = """
                    ShoumHome membantu Anda untuk membuat pengumuman dan informasi terkait kajian yang akan Anda adakan! Selain itu, Anda juga dapat memuat posting islami yang akan disebarkan ke seluruh kalangan masyarakat melalui ShoumHome! 
                """.trimIndent(),
                titleColor = Color.BLACK,
                imageDrawable = R.drawable.intro1,
                descriptionColor = Color.BLACK,
                backgroundColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Membantu mereka yang tersesat!",
                description = """
                    Daftarkan masjid-masjid yang Anda kelola atau sering Anda kunjungi agar mereka tidak kehilangan arah menuju masjid dan juga agar mereka tau dimana saja Anda berdakwah dan beribadah!
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
//        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//        with (sp.edit()) {
//            putBoolean("first", true)
//            commit()
//        }
//        val home = Intent(this@IntroActivity, LayarUtamaActivity::class.java)
//        startActivity(home)
//        finish()
    }
}
