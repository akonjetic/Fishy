package hr.konjetic.fishy.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.User
import hr.konjetic.fishy.network.model.UserPost
import kotlinx.coroutines.launch

class LoginActivityViewModel : ViewModel() {

    val listOfUsers = MutableLiveData<ArrayList<User>>()
    val user = MutableLiveData<User>()

    fun getAllUsers(){
        viewModelScope.launch {
            listOfUsers.value = Network().getService().getAllUsers() as ArrayList<User>
        }
    }

    fun createNewUser(data: UserPost){
        viewModelScope.launch {
            Network().getService().postUser(data)
        }
    }

    fun getUserByUsername(username : String){
        viewModelScope.launch {
            val response = Network().getService().getByUsername(username)

            if (response.isSuccessful){
                user.value = response.body()
            }
        }
    }
}