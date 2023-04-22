package hr.konjetic.fishy.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import hr.konjetic.fishy.R
import hr.konjetic.fishy.database.User
import hr.konjetic.fishy.database.UserType
import hr.konjetic.fishy.databinding.ActivityLoginBinding
import hr.konjetic.fishy.databinding.ActivityLoginSignupBinding
import hr.konjetic.fishy.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingSignup: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //spremanje podataka o loginu
        val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        //binding view postavljanje
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        bindingSignup = ActivityLoginSignupBinding.inflate(layoutInflater)
        val viewSignup = bindingSignup.root


        //provjera jel već ulogiran
        if (isLoggedIn) {
            redirectToMain()
        } else {
            setContentView(view)
        }

        //skrivanje action bara u dark modeu i prislini light mode
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //login button
        binding.loginButton.setOnClickListener {
            if (authenticateUser(email = binding.emailEt.text.toString(), password = binding.passET.text.toString()) != null){
                val editor = sharedPreferences.edit()
                editor.putBoolean("is_logged_in", true)
                editor.apply()

                redirectToMain()
            } else{
                Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show()
            }

        }

        //sign up promjena screena
        binding.signUpText.setOnClickListener {
            setContentView(viewSignup)
        }

        //login promjena screena
        bindingSignup.logintText.setOnClickListener {
            setContentView(view)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.color_primary)
        }

    }

    //provjera je li postojeći user
    private fun authenticateUser(email: String, password: String): User? {
        val users = listOf(
            User("anakonjetic@gmail.com", "ana123", UserType.BASIC),
            User("admin@gmail.com", "admin123", UserType.ADMIN)
        )
        return users.find { it.email == email && it.password == password }
    }

    //preusmjeravanje u main screen
    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}