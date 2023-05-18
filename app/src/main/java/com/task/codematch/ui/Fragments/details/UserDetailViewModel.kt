package com.task.codematch.ui.Fragments.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.repository.UsersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UsersRepositoryImpl
) : ViewModel() {

    private val _state = MutableLiveData<Resource<User>?>()
    val _uiState: MutableLiveData<Resource<User>?> = _state


    fun getUserDetail(userId: Long) {
        viewModelScope.launch {
            userRepository.getUserById(userId).distinctUntilChanged().collect { result ->
                if (result == null) {
                    _uiState.value = Resource.Failed("")
                } else {
                    _uiState.value = Resource.Success(result)
                }
            }
        }
    }
}