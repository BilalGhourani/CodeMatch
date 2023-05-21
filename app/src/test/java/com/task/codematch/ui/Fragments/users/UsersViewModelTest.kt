package com.task.codematch.ui.Fragments.users

import com.google.common.truth.ExpectFailure
import com.google.common.truth.Truth
import com.task.codematch.data.source.local.entity.Address
import com.task.codematch.data.source.local.entity.Company
import com.task.codematch.data.source.local.entity.Geo
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.repository.UsersRepositoryImplTest
import com.google.common.truth.Truth.assertThat
import getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UsersViewModelTest {

    @get:Rule
    private lateinit var usersViewModel: UsersViewModel
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
        ), User(
            2,
            "Jane", "Jane_username",
            "Jane@xxx.com",
            "837419712",
            "Jane.com",
            Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
            Company("name", "catch_phrase", "bs"),
            isFavorite = 0
        )
    )
    private val sampleUser = usersList[0]

    @Before
    fun setupViewModel() {
        usersViewModel = UsersViewModel(fakeUsersRepositoryImpl)
    }

    @After
    fun clearViewModel() {
        usersViewModel.clear()
    }

    @Test
    fun getAllUsers_returnsAllUsersList() =
        runTest(UnconfinedTestDispatcher()) {
            usersViewModel.getAllUsers()
            val receivedList = usersViewModel.users.getOrAwaitValueTest()
            assertThat(receivedList).isEqualTo(Resource.Success(usersList))
        }

    @Test
    fun toggleFavoriteValue(user: User) = runTest(UnconfinedTestDispatcher()) {
        val isFavorite = user.isFavorite
        if (isFavorite == 0) {
            user.isFavorite = 1
        } else {
            user.isFavorite = 0
        }
       assertThat(user.isFavorite).isNotEqualTo(isFavorite)
    }

}