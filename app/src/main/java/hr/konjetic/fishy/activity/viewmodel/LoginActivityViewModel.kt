package hr.konjetic.fishy.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.User
import hr.konjetic.fishy.network.model.UserPost
import kotlinx.coroutines.launch

class LoginActivityViewModel : ViewModel() {

    val listOfUsers = MutableLiveData<ArrayList<User>>()

    fun getAllUsers() {
        viewModelScope.launch {
            listOfUsers.value = Network().getService().getAllUsers()
        }
    }

    fun createNewUser(data: UserPost) {
        viewModelScope.launch {
            Network().getService().postUser(data)
        }
    }

}