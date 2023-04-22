package hr.konjetic.fishy.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import hr.konjetic.fishy.R
import hr.konjetic.fishy.databinding.ActivityMainBinding
import hr.konjetic.fishy.fragment.activityMain.AquariumFragment
import hr.konjetic.fishy.fragment.activityMain.FavoritesFragment
import hr.konjetic.fishy.fragment.activityMain.HomeFragment
import hr.konjetic.fishy.fragment.activityMain.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding view postavljanje
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //skrivanje action bara u dark modeu i prisilni light mode
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val homeFragment = HomeFragment()
        val aquariumFragment = AquariumFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        //logika za šaltanje u bottom navigationu
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_aquarium -> setCurrentFragment(aquariumFragment)
                R.id.ic_favorites -> setCurrentFragment(favoritesFragment)
                R.id.ic_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }

        //qr code scanner button i logika
        val scanBtn = binding.fab
        scanBtn.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setCameraId(0)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.initiateScan()
        }


        //status i navigacijski bar boje
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.color_primary)
        }
    }

    //bottom nav logika
    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }

    //otvaranje linka koji je u qr-u
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            val url = result.contents
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}