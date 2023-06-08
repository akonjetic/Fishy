package hr.konjetic.fishy.fragment.activityMain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.adapter.FavoritesFragmentAdapter
import hr.konjetic.fishy.adapter.FishSearchAdapter
import hr.konjetic.fishy.adapter.HomeFishPagingAdapter
import hr.konjetic.fishy.adapter.HomeFragmentAdapter
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.database.entities.FavoriteFishFamily
import hr.konjetic.fishy.database.entities.FavoriteHabitat
import hr.konjetic.fishy.database.entities.FavoriteWaterType
import hr.konjetic.fishy.databinding.FragmentHomeBinding
import hr.konjetic.fishy.network.paging.FishDiff
import hr.konjetic.fishy.network.paging.FishPagingSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterSearchFish by lazy { FishSearchAdapter(requireContext(), arrayListOf()) }
    private val adapterFishList by lazy { HomeFragmentAdapter(requireContext(), arrayListOf()) }

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

      /*  lifecycleScope.launch {
            viewModel.fishFlow.collectLatest {
                fishPagingAdapter.submitData(it)
            }
        }*/


            lifecycleScope.launch {
                viewModel.refreshFishFlow().collectLatest {
                    fishPagingAdapter.submitData(it)
                }
            }

        viewModel.getAllFishFamilyIncompatibleData()


        /*viewModel.listOfFish.observe(viewLifecycleOwner){
            adapterFishList.updateFavorites(it)
        }*/


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

    fun setupSearch(){
        binding.searchResultsRecycler.adapter = adapterSearchFish

        viewModel.fishSearchResult.observe(viewLifecycleOwner){
            adapterSearchFish.updateFishList(it)
        }

        binding.inputFieldSearchText.doAfterTextChanged {
            if (binding.inputFieldSearchText.text.toString() == ""){
                binding.searchResultsRecycler.visibility = View.GONE
            } else{
                binding.searchResultsRecycler.visibility = View.VISIBLE
                viewModel.getFishByName(binding.inputFieldSearchText.text.toString())
            }
        }
    }



}