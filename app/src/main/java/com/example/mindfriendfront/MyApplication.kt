package com.example.mindfriendfront

import android.app.Application
import com.example.mindfriendfront.network.ApiServiceFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiServiceFactory // ApiServiceFactory를 초기화합니다.
    }
}
