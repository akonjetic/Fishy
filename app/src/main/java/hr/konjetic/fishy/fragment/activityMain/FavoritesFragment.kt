package hr.konjetic.fishy.fragment.activityMain

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.adapter.FavoritesFragmentAdapter
import hr.konjetic.fishy.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoriteFishAdapter by lazy {FavoritesFragmentAdapter(requireContext(), arrayListOf())}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPreferences = requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.recyclerFavoriteFish.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavoriteFish.adapter = favoriteFishAdapter

        viewModel.getFavoriteFish(requireContext(), sharedPreferences.getInt("user_id", 1))

        viewModel.listOfFavoriteFish.observe(viewLifecycleOwner){
                favoriteFishAdapter.updateFavorites(it)

        }


        return binding.root
    }

    override fun onResume() {

        val sharedPreferences = requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        viewModel.getFavoriteFish(requireContext(), sharedPreferences.getInt("user_id", 1))

        super.onResume()


    }
}
