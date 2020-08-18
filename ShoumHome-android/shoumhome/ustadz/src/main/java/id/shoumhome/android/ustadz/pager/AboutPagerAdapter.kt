package id.shoumhome.android.ustadz.pager

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.fragments.DevelopersFragment

class AboutPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
            R.string.leader,
            R.string.member_one,
            R.string.member_two
    )

    override fun getPageTitle(position: Int): CharSequence? = context.resources.getString(tabTitles[position])

    override fun getCount(): Int = tabTitles.count()

    override fun getItem(position: Int): Fragment = DevelopersFragment.newInstance(position)

}