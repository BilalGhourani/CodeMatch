package com.task.codematch.ui.Fragments.details

import com.google.common.truth.Truth
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.repository.UsersRepositoryImplTest
import getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UserDetailViewModelTest {

    private lateinit var userDetailViewModel: UserDetailViewModel
    private val fakeUsersRepositoryImpl = UsersRepositoryImplTest()

    @Before
    fun setupViewModel() {
        userDetailViewModel = UserDetailViewModel(fakeUsersRepositoryImpl)
    }

    @After
    fun clearViewModel() {
        userDetailViewModel.clear()
    }

    @Test
    fun getUserDetail(userId: Long) =
        runTest(UnconfinedTestDispatcher()) {
            userDetailViewModel.getUserDetail(userId)
            val user = userDetailViewModel._uiState.getOrAwaitValueTest()
            Truth.assertThat(user).isEqualTo(userId)
        }

    @Test
    fun toggleFavoriteValue(user: User) = runTest(UnconfinedTestDispatcher()) {
        val isFavorite = user.isFavorite
        if (isFavorite == 0) {
            user.isFavorite = 1
        } else {
            user.isFavorite = 0
        }
        Truth.assertThat(user.isFavorite).isNotEqualTo(isFavorite)
    }
}