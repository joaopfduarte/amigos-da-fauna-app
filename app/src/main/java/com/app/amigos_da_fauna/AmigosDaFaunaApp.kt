package com.app.amigos_da_fauna

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class AmigosDaFaunaApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        SingletonImageLoader.setSafe { newImageLoader(it) }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = OkHttpClient()))
            }
            .build()
    }
}
