package com.laundrybuoy.customer.di

import com.laundrybuoy.customer.BuildConfig
import com.laundrybuoy.customer.retrofit.CustomerAPI
import com.laundrybuoy.customer.utils.Constants.BASE_URL
import com.laundrybuoy.customer.utils.NetworkConnectionInterceptor
import com.laundrybuoy.customer.utils.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return logging
    }

    @Provides
    @Singleton
    fun providesNetworkInterceptor() = NetworkConnectionInterceptor()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NetworkConnectionInterceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES) // write timeout
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader(
                        "role", "customer"
                    ).addHeader(
                        "Authorization", "Bearer ${SharedPreferenceManager.getBearerToken()}"
                    )
                    .build()
            )
        }
        .addInterceptor(networkInterceptor)

        .build()


    @Singleton
    @Provides
    fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun getQuoteAPI(retrofit: Retrofit): CustomerAPI {
        return retrofit.create(CustomerAPI::class.java)
    }
}