package hr.konjetic.fishy.activity

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

            if (authenticateUser(username = binding.emailEt.text.toString(), password = binding.passET.text.toString())){

                if (checkIfAdmin(binding.emailEt.text.toString())){
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.apply()
                    editor.putBoolean("is_admin", true)
                    editor.apply()

                    redirectToAdmin()
                } else{
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.apply()
                    editor.putBoolean("is_admin", false)
                    editor.apply()
                    editor.putInt("user_id", getUserId(username = binding.emailEt.text.toString()))
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

        //sign up lodgija
        bindingSignup.button.setOnClickListener {
            if (checkIfEmpty(username = bindingSignup.usernameET.text.toString(), email = bindingSignup.emailEt.text.toString(), password = bindingSignup.passET.text.toString())){
                Toast.makeText(this, "All fields need to be entered.", Toast.LENGTH_SHORT).show()
            } else if (checkIfUserExists(email = bindingSignup.emailEt.text.toString(), username = bindingSignup.usernameET.text.toString())){
                Toast.makeText(this, "User with this Username or Email already exists.", Toast.LENGTH_SHORT).show()
            } else if (!checkIfPasswordMatches(password = bindingSignup.passET.text.toString(), passwordConfirmed = bindingSignup.confirmPassEt.text.toString())){
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "User created successfully.", Toast.LENGTH_SHORT).show()

                val editor = sharedPreferences.edit()
                editor.putBoolean("is_logged_in", true)
                editor.apply()

                viewModel.createNewUser(
                    UserPost(
                        email = bindingSignup.emailEt.text.toString(),
                        username = bindingSignup.usernameET.text.toString(),
                        userType = 1,
                        password = bindingSignup.passET.text.toString()
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
        val existsUsername = viewModel.listOfUsers.value?.any{it.username == username}
        if(existsUsername!!){
            val user = viewModel.listOfUsers.value?.find { it.username == username }
            if (user?.password == password){
                return true
            }
            return false
        }
        return false
    }

    //provjera je li postojeći user
    private fun checkIfUserExists(username: String, email:String) : Boolean{
        val existsUsername = viewModel.listOfUsers.value?.any{it.username == username}
        val existsEmail = viewModel.listOfUsers.value?.any{it.email == email}

        return (existsEmail!! || existsUsername!!)
    }

    //podudaranje lozinki na sign up
    private fun checkIfPasswordMatches(password: String, passwordConfirmed: String) : Boolean{
        return password.equals(passwordConfirmed)
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