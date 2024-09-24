package com.ivanbartolelli.kotlinrepos

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.GsonBuilder
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.PostEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.DatabaseConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.mediators.PostRemoteMediator
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.PostService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PostsRepo
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PostsRepoImpl
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RemoteMediatorTest {

    private lateinit var postsRepo: PostsRepo
    private val mockDb = provideDatabase(ApplicationProvider.getApplicationContext())
    private val service = provideAuthenticationService(provideRetrofitClient(provideOkHttpClient()))

    @Before
    fun createRepo() {
        postsRepo = provideRepositoriesRepo(
            service,
            mockDb
        )
    }

    @After
    fun clearDb() {
        mockDb.clearAllTables()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun remoteMediator_appendsDataSuccessfully() = runBlocking {
        // Add mock results for the API to return.

        val remoteMediator = PostRemoteMediator(service, mockDb)

        val pagingState = PagingState<Int, PostEntity>(
            listOf(),
            null,
            PagingConfig(30),
            10
        )
        val result = remoteMediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    private fun provideOkHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .readTimeout(BuildConfig.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(BuildConfig.WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(BuildConfig.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    private fun provideRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .client(client)
            .build()
    }

    private fun provideAuthenticationService(retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    private fun provideRepositoriesRepo(service: PostService, database: PostsDatabase): PostsRepo {
        return PostsRepoImpl(service, database)
    }

    private fun provideDatabase(application: Application): PostsDatabase {
        return Room.databaseBuilder(application, PostsDatabase::class.java, DatabaseConstants.DATABASE_NAME).build()
    }
}