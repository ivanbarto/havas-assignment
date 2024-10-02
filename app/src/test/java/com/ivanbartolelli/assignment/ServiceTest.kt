package com.ivanbartolelli.assignment

import com.google.gson.GsonBuilder
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.services.PostService
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ServiceTest {

    @Test
    fun webService_isWorking() = runTest {
        val itemsPerPage = 30
        val service = provideAuthenticationService(
            provideRetrofitClient(
                provideOkHttpClient()
            )
        )

        val posts = service.getPostsPage(
            nextId = null,
            limit = itemsPerPage
        )

        assertEquals(itemsPerPage, posts.data.children.count())
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

}