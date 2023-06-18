package hr.konjetic.fishy.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import hr.konjetic.fishy.activity.viewmodel.AdminActivityViewModel
import hr.konjetic.fishy.adapter.AdminActivityAdapter
import hr.konjetic.fishy.databinding.ActivityAdminBinding
import hr.konjetic.fishy.fragment.activityAdmin.ManageFishFragment
import hr.konjetic.fishy.fragment.activityAdmin.NewFishFragment

class AdminActivity : AppCompatActivity(), NewFishFragment.OnFishCreatedListener {

    private lateinit var binding: ActivityAdminBinding
    private val viewModel : AdminActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        val view = binding.root

        viewModel.getAllFishFamilies()
        viewModel.getAllHabitats()
        viewModel.getAllWaterTypes()


        setContentView(view)

        //tab layout setup
        val adminActivityAdapter = AdminActivityAdapter(supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = adminActivityAdapter
        val tabs: TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)

        //logout
        binding.logoutButton.setOnClickListener {
            val sharedPreferences = this.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.apply()
            editor.putBoolean("is_admin", false)
            editor.apply()

            redirectToLogin()
        }
    }

    private fun redirectToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFishCreated() {
        val manageFishFragment = supportFragmentManager.findFragmentByTag("manageFishFragment")
        if (manageFishFragment is ManageFishFragment) {
            manageFishFragment.refreshData()
        }
    }
}