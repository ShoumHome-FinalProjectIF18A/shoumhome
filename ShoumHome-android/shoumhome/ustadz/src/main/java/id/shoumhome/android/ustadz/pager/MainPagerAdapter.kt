package id.shoumhome.android.ustadz.pager

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.fragments.ArticlesFragment
import id.shoumhome.android.ustadz.fragments.KajianFragment
import id.shoumhome.android.ustadz.fragments.MosqueListFragment

class MainPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(
            R.string.tab_article,
            R.string.tab_kajian,
            R.string.tab_mosque
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ArticlesFragment()
            1 -> fragment = KajianFragment()
            2 -> fragment = MosqueListFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 3

}