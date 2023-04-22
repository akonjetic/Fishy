package hr.konjetic.fishy.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.konjetic.fishy.R
import hr.konjetic.fishy.fragment.activityAdmin.NewFishFragment
import hr.konjetic.fishy.fragment.activityAdmin.NewUserFragment

private val titles = arrayOf(
    "NEW FISH",
    "NEW USER"
)

class AdminActivityAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return  titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> NewFishFragment()
            else -> NewUserFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

}