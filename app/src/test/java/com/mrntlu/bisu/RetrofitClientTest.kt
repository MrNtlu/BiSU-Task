package com.mrntlu.bisu

import com.mrntlu.bisu.service.NewsApiService
import com.mrntlu.bisu.util.Constants
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientTest {

    @Test
    fun testNewsApiServiceCall() {
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)

        runBlocking {

            val response = service.getBreakingNews("us", 1)

            assert(response.status == "ok")
            assert(response.articles.isNotEmpty())
        }
    }
}