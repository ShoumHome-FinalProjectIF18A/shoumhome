package id.shoumhome.android.pager

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.shoumhome.android.R
import id.shoumhome.android.fragments.MosqueListFragment
import id.shoumhome.android.fragments.MosqueLocationMapFragment

class MosqueLocationPagerAdapter(private val mContext: Context, fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
            R.string.mosque_location,
            R.string.mosque_list
    )

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabTitles[position])

    override fun getCount(): Int = tabTitles.count()

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = when (position) {
            0 -> MosqueLocationMapFragment()
            1 -> MosqueListFragment()
            else -> null
        }
        return fragment as Fragment
    }

}