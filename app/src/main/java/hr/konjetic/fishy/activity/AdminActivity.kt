package hr.konjetic.fishy.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import hr.konjetic.fishy.R
import hr.konjetic.fishy.adapter.AdminActivityAdapter
import hr.konjetic.fishy.databinding.ActivityAdminBinding
import hr.konjetic.fishy.databinding.ActivityMainBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //tab layout setup
        val adminActivityAdapter = AdminActivityAdapter(this, supportFragmentManager)
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
}