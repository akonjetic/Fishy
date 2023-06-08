package hr.konjetic.fishy.adapter

import android.content.Context
import android.os.Bundle
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
        val fragment = AquariumTabFragment()
        val args = Bundle()
        args.putInt("position", position)
        fragment.arguments = args
        return fragment
    }

    override fun getItemId(position: Int): Long {
        // Return a unique identifier for each item
        return position.toLong()
    }

    override fun getItemPosition(`object`: Any): Int {
        // Return POSITION_NONE to force recreation of the Fragment
        return POSITION_NONE
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}