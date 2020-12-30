package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.WorldTidesGateway
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class RetrofitClient(baseUrl: String) {
    private val moshiFactory = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val tidesService: WorldTidesGateway by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshiFactory))
            .build()
            .create(WorldTidesGateway::class.java)
    }
}
