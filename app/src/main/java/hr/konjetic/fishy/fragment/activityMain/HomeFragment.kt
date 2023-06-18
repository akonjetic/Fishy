package hr.konjetic.fishy.fragment.activityMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.adapter.FishSearchAdapter
import hr.konjetic.fishy.adapter.HomeFishPagingAdapter
import hr.konjetic.fishy.databinding.FragmentHomeBinding
import hr.konjetic.fishy.network.paging.FishDiff
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterSearchFish by lazy { FishSearchAdapter(requireContext(), arrayListOf()) }

    private val fishPagingAdapter by lazy { HomeFishPagingAdapter(requireContext(), FishDiff) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val fishPagingAdapter = HomeFishPagingAdapter(requireContext(), FishDiff)

        binding.searchResultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fishRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fishRecycler.adapter = fishPagingAdapter
        setupSearch()

        lifecycleScope.launch {
            viewModel.refreshFishFlow().collectLatest {
                fishPagingAdapter.submitData(it)
            }
        }

        viewModel.getAllFishFamilyIncompatibleData()


        return binding.root
    }

    override fun onResume() {


        lifecycleScope.launch {
            viewModel.refreshFishFlow().collectLatest {
                fishPagingAdapter.submitData(it)
            }
        }

        super.onResume()
    }

    fun setupSearch() {
        binding.searchResultsRecycler.adapter = adapterSearchFish

        viewModel.fishSearchResult.observe(viewLifecycleOwner) {
            adapterSearchFish.updateFishList(it)
        }

        binding.inputFieldSearchText.doAfterTextChanged {
            if (binding.inputFieldSearchText.text.toString() == "") {
                binding.searchResultsRecycler.visibility = View.GONE
            } else {
                binding.searchResultsRecycler.visibility = View.VISIBLE
                viewModel.getFishByName(binding.inputFieldSearchText.text.toString())
            }
        }
    }




}