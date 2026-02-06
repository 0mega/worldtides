package com.oleksandrkruk.worldtides

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class RetrofitClient(
        baseUrl: String,
        private val customClient: OkHttpClient? = null,
        private val loggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
) {
    private val moshiFactory = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val client: OkHttpClient by lazy {
        customClient
                ?: run {
                    val logging = HttpLoggingInterceptor()
                    logging.level = loggingLevel
                    OkHttpClient.Builder().addInterceptor(logging).build()
                }
    }

    val tidesService: WorldTidesGateway by lazy {
        Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshiFactory))
                .build()
                .create(WorldTidesGateway::class.java)
    }
}
