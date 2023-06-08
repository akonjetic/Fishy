package hr.konjetic.fishy.fragment.activityMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import hr.konjetic.fishy.activity.MainActivity
import hr.konjetic.fishy.adapter.AquariumFragmentAdapter
import hr.konjetic.fishy.databinding.FragmentAquariumBinding
import java.util.*


class AquariumFragment : Fragment() {

    private var _binding: FragmentAquariumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAquariumBinding.inflate(inflater, container, false)

        val aquariumFragmentAdapter = AquariumFragmentAdapter(requireContext(), childFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = aquariumFragmentAdapter
        val tabs : TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)


        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selection
                val selectedTabPosition = when (tab?.text){
                    "FIRST" -> 0
                    "SECOND" -> 1
                    else -> 2
                }

                println("################ $selectedTabPosition")
                viewPager.currentItem = selectedTabPosition
                // Do something based on the selected tab position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselection (optional)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselection (optional)
            }
        })




        return binding.root
    }


    override fun onResume() {
        super.onResume()

        val tabLayout = binding.tabLayout
        val tab = tabLayout.getTabAt(0)
        tab?.select()
    }



}