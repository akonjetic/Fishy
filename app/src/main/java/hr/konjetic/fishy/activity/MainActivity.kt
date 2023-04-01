package hr.konjetic.fishy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import hr.konjetic.fishy.R
import hr.konjetic.fishy.databinding.ActivityMainBinding
import hr.konjetic.fishy.fragment.AquariumFragment
import hr.konjetic.fishy.fragment.FavoritesFragment
import hr.konjetic.fishy.fragment.HomeFragment
import hr.konjetic.fishy.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment()
        val aquariumFragment = AquariumFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_aquarium -> setCurrentFragment(aquariumFragment)
                R.id.ic_favorites -> setCurrentFragment(favoritesFragment)
                R.id.ic_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }
}