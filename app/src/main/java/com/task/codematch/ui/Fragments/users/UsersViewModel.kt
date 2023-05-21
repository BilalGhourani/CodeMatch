package com.task.codematch.ui.Fragments.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableLiveData<Resource<MutableList<User>>?>()
    val users: MutableLiveData<Resource<MutableList<User>>?> = _users

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllUsers()
                .map { resource -> Resource.Success(resource) }
                .collect { result ->
                    viewModelScope.launch(Dispatchers.Main) {
                        _users.value = result.data
                    }
                }
        }
    }

    fun toggleFavoriteValue(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user.isFavorite == 0) {
                user.isFavorite = 1
            } else {
                user.isFavorite = 0
            }
            userRepository.saveUser(user)
        }
    }

    fun clear() {
        this.onCleared()
    }
}