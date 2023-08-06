// MyApplication.kt 파일
package com.example.mindfriendfront

import android.app.Application
import android.content.Context
import com.example.mindfriendfront.network.ApiServiceFactory

class MyApplication : Application() {
    companion object {
        lateinit var context: Context // Application context를 전역으로 사용하기 위한 변수
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext // Application context를 할당
    }
}
