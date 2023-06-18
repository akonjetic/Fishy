package hr.konjetic.fishy.fragment.activityAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.konjetic.fishy.activity.viewmodel.AdminActivityViewModel
import hr.konjetic.fishy.adapter.ManageFishPagingAdapter
import hr.konjetic.fishy.databinding.FragmentManageFishBinding
import hr.konjetic.fishy.network.model.Fish
import hr.konjetic.fishy.network.paging.FishDiff
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ManageFishFragment : Fragment(), ManageFishPagingAdapter.OnDeleteClickListener {

    private var _binding: FragmentManageFishBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminActivityViewModel by activityViewModels()

    private lateinit var fishPagingAdapter: ManageFishPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageFishBinding.inflate(inflater, container, false)

        setupRecyclerView()
        refreshData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        fishPagingAdapter = ManageFishPagingAdapter(requireContext(), FishDiff)
        fishPagingAdapter.setOnDeleteClickListener(this)
        binding.fishRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fishRecycler.adapter = fishPagingAdapter
    }

    override fun onDeleteClicked(item: Fish) {
        viewModel.deleteFish(item.id)

        refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshFishFlow().collectLatest { pagingData ->
                fishPagingAdapter.submitData(pagingData)
            }
        }

        binding.swipeRefreshLayout.isRefreshing = false

    }
}