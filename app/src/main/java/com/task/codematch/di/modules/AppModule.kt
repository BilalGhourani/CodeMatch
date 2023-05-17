package com.task.codematch.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.task.codematch.data.source.local.AppDatabase
import com.task.codematch.data.source.remote.UserService
import com.task.codematch.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "mydb.db"
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.UserDao()

    @Provides
    @Singleton
    fun provideApi(gson: Gson, client: OkHttpClient.Builder): UserService =
        Retrofit
            .Builder()
            .client(client.build())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)

}