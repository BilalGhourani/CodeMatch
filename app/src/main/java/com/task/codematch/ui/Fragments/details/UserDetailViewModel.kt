package com.task.codematch.ui.Fragments.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.repository.UsersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UsersRepositoryImpl
) : ViewModel() {

    val _state = MutableLiveData<Resource<User>?>()
    val _uiState: MutableLiveData<Resource<User>?> = _state

    fun notifyModel(user: User) {
        _uiState.value = Resource.Success(user)
    }

    fun getUserDetail(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserById(userId).distinctUntilChanged().collect { result ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (result == null) {
                        _uiState.value = Resource.Failed("")
                    } else {
                        _uiState.value = Resource.Success(result)
                    }
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