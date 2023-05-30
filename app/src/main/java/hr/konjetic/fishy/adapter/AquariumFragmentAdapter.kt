package hr.konjetic.fishy.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.konjetic.fishy.fragment.activityMain.AquariumFragment
import hr.konjetic.fishy.fragment.activityMain.AquariumTabFragment

private val titles = arrayOf(
    "FIRST",
    "SECOND",
    "THIRD"
)

class AquariumFragmentAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> AquariumTabFragment(0)
            1 -> AquariumTabFragment(1)
            else -> AquariumTabFragment(2)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}