package hr.konjetic.fishy.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.LoginActivityViewModel
import hr.konjetic.fishy.databinding.ActivityLoginBinding
import hr.konjetic.fishy.databinding.ActivityLoginSignupBinding
import hr.konjetic.fishy.network.model.UserPost

const val ADMIN = 0

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingSignup: ActivityLoginSignupBinding
    private val viewModel : LoginActivityViewModel by viewModels()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //spremanje podataka o loginu
        val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val isAdmin = sharedPreferences.getBoolean("is_admin", false)


        //binding view postavljanje
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        bindingSignup = ActivityLoginSignupBinding.inflate(layoutInflater)
        val viewSignup = bindingSignup.root



        //provjera jel već ulogiran
        if (isLoggedIn) {
            if (isAdmin){
                redirectToAdmin()
            }else{
                redirectToMain()
            }
        } else {
            setContentView(view)
        }

        //skrivanje action bara u dark modeu i prislini light mode
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //dohvaćanje User podataka s API
        viewModel.getAllUsers()


        //login button
        binding.loginButton.setOnClickListener {
            val username = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            if (authenticateUser(username = username, password = password)){

                if (checkIfAdmin(username)){
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.putBoolean("is_admin", true)
                    editor.apply()

                    redirectToAdmin()
                } else{
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.putBoolean("is_admin", false)
                    editor.putInt("user_id", getUserId(username))
                    editor.apply()

                    redirectToMain()
                }

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

        //sign up logina
        bindingSignup.button.setOnClickListener {
            val username = bindingSignup.usernameET.text.toString()
            val email = bindingSignup.emailEt.text.toString()
            val password = bindingSignup.passET.text.toString()
            val confirmPassword = bindingSignup.confirmPassEt.text.toString()

            if (checkIfEmpty(username, email, password)) {
                Toast.makeText(this, "All fields need to be entered.", Toast.LENGTH_SHORT).show()
            } else if (checkIfUserExists(email, username)) {
                Toast.makeText(this, "User with this Username or Email already exists.", Toast.LENGTH_SHORT).show()
            } else if (!checkIfPasswordMatches(password, confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "User created successfully.", Toast.LENGTH_SHORT).show()

                val editor = sharedPreferences.edit().apply {
                    putBoolean("is_logged_in", true)
                }
                editor.apply()

                viewModel.createNewUser(
                    UserPost(
                        email = email,
                        username = username,
                        userType = 1,
                        password = password
                    )
                )

                redirectToMain()
            }
        }


        //status i navigacijska boja
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.color_primary)
        }

    }

    //autentikacija
    private fun authenticateUser(username: String, password: String): Boolean {
        val user = viewModel.listOfUsers.value?.find { it.username == username }
        return user?.password == password
    }

    //provjera je li postojeći user
    private fun checkIfUserExists(username: String, email:String) : Boolean{
        val existsUsername = viewModel.listOfUsers.value?.any{it.username == username}
        val existsEmail = viewModel.listOfUsers.value?.any{it.email == email}

        return (existsEmail!! || existsUsername!!)
    }

    //podudaranje lozinki na sign up
    private fun checkIfPasswordMatches(password: String, passwordConfirmed: String) : Boolean{
        return password == passwordConfirmed
    }

    //popunjenost polja na sign up
    private fun checkIfEmpty(username: String, email: String, password: String): Boolean{
        return (username.isEmpty() || email.isEmpty() || password.isEmpty())
    }

    //preusmjeravanje u main screen
    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToAdmin() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getUserId(username: String) : Int{
        val user = viewModel.listOfUsers.value?.find { it.username == username }
        return user!!.id
    }

    //usertype provjera - admin
    private fun checkIfAdmin(username: String) : Boolean{
        val existsUsername = viewModel.listOfUsers.value?.any{it.username == username}
        if(existsUsername!!){
            val user = viewModel.listOfUsers.value?.find { it.username == username }
            return user?.userType == ADMIN
        }
        return false
    }
}