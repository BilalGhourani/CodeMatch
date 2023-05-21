package com.task.codematch.ui.Fragments.favorites

import com.google.common.truth.Truth
import com.task.codematch.data.source.local.entity.Address
import com.task.codematch.data.source.local.entity.Company
import com.task.codematch.data.source.local.entity.Geo
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
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
internal class FavoritesViewModelTest {

    @get:Rule
    private lateinit var favoritesViewModel: FavoritesViewModel
    private val fakeUsersRepositoryImpl = UsersRepositoryImplTest()

    private val usersList = mutableListOf(
        User(
            1,
            "John", "John_username",
            "John@xxx.com",
            "8084098134",
            "John.com",
            Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
            Company("name", "catch_phrase", "bs"),
            isFavorite = 1
        )
    )

    @Before
    fun setupViewModel() {
        favoritesViewModel = FavoritesViewModel(fakeUsersRepositoryImpl)
    }

    @After
    fun clearViewModel() {
        favoritesViewModel.clear()
    }

    @Test
    fun getFavoritesUsers_returnsAllFavoriteUsersList() =
        runTest(UnconfinedTestDispatcher()) {
            favoritesViewModel.getFavoritesUsers()
            val receivedList = favoritesViewModel.users.getOrAwaitValueTest()
            Truth.assertThat(receivedList).isEqualTo(Resource.Success(usersList))
        }

    @Test
    fun unFavoriteValue(user: User) = runTest(UnconfinedTestDispatcher()) {
        user.isFavorite = 0
        Truth.assertThat(user.isFavorite).isNotEqualTo(0)
    }
}