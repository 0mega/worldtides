package com.oleksandrkruk.worldtides

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class RetrofitClientTest {

    @Test
    fun `uses provided custom OkHttpClient`() {
        val customClient = OkHttpClient.Builder().build()
        val retrofitClient = RetrofitClient("https://example.com/", customClient = customClient)

        assertSame(customClient, retrofitClient.client)
    }

    @Test
    fun `uses provided logging level when no custom client`() {
        val level = HttpLoggingInterceptor.Level.BODY
        val retrofitClient = RetrofitClient("https://example.com/", loggingLevel = level)

        val client = retrofitClient.client
        val interceptor =
                client.interceptors().find { it is HttpLoggingInterceptor } as?
                        HttpLoggingInterceptor

        // Ensure interceptor exists and has correct level
        if (interceptor != null) {
            assertEquals(level, interceptor.level)
        } else {
            // Fail if interceptor not found (or maybe we expect it to be there)
            // The logic adds it unconditionally.
            throw AssertionError("HttpLoggingInterceptor not found")
        }
    }

    @Test
    fun `custom client takes precedence over logging level`() {
        val customClient = OkHttpClient.Builder().build()
        val level = HttpLoggingInterceptor.Level.BODY // Different from default

        val retrofitClient =
                RetrofitClient(
                        "https://example.com/",
                        customClient = customClient,
                        loggingLevel = level
                )

        assertSame(customClient, retrofitClient.client)
    }

    @Test
    fun `uses default behavior with no config`() {
        val retrofitClient = RetrofitClient("https://example.com/")

        val client = retrofitClient.client
        val interceptor =
                client.interceptors().find { it is HttpLoggingInterceptor } as?
                        HttpLoggingInterceptor

        if (interceptor != null) {
            assertEquals(HttpLoggingInterceptor.Level.NONE, interceptor.level)
        } else {
            throw AssertionError("HttpLoggingInterceptor not found")
        }
    }
}
