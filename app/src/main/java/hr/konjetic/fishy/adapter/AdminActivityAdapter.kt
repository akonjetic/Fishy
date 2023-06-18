package hr.konjetic.fishy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.konjetic.fishy.fragment.activityAdmin.NewFishFragment
import hr.konjetic.fishy.fragment.activityAdmin.ManageFishFragment

private val titles = arrayOf(
    "NEW FISH",
    "MANAGE FISH"
)

class AdminActivityAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return  titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> NewFishFragment()
            else -> ManageFishFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}