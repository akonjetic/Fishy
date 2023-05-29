package hr.konjetic.fishy.fragment.activityMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import hr.konjetic.fishy.adapter.AquariumFragmentAdapter
import hr.konjetic.fishy.databinding.FragmentAquariumBinding


class AquariumFragment : Fragment() {

    private var _binding: FragmentAquariumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAquariumBinding.inflate(inflater, container, false)

        val aquariumFragmentAdapter = AquariumFragmentAdapter(requireContext(), requireFragmentManager())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = aquariumFragmentAdapter
        val tabs : TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)

        tabs.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return binding.root
    }


}