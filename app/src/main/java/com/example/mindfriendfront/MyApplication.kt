package com.example.mindfriendfront

import android.app.Application
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mindfriendfront.network.ApiServiceFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // ApiServiceFactory를 초기화합니다.
        ApiServiceFactory.initialize(this)
    }
}
