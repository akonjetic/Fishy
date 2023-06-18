package hr.konjetic.fishy.fragment.activityMain

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import hr.konjetic.fishy.activity.LoginActivity
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val sharedPreferences =
            requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        //log out button logika
        binding.logoutButton.setOnClickListener {
            logout()
            redirectToLogin()
        }

        binding.clearFavoritesButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder
                .setTitle("Clear favorites?")
                .setPositiveButton(
                    "CLEAR"
                ) { _, _ ->
                    viewModel.removeFavoriteFishOfUserFromDatabaseCascade(requireContext(), userId)
                    Snackbar.make(binding.root, "All favorites are removed", Snackbar.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
                .create()

            dialog.show()
        }

        binding.clearAquariumsButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder
                .setTitle("Clear aquariums?")
                .setPositiveButton(
                    "CLEAR"
                ) { _, _ ->
                    viewModel.deleteAquariumsOfUser(requireContext(), userId)
                    Snackbar.make(binding.root, "All aquariums are cleared", Snackbar.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
                .create()

            dialog.show()
        }

        return binding.root
    }

    private fun logout() {
        val sharedPreferences =
            this.activity?.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("is_logged_in", false)
        editor?.apply()
    }

    private fun redirectToLogin() {
        val intent = Intent(this.activity, LoginActivity::class.java)
        startActivity(intent)
    }
}