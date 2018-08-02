package io.incepted.ultrafittimer.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.support.HasSupportFragmentInjector
import io.incepted.ultrafittimer.UltraFitApp

object AppInjector {
    fun init(ultraFitApp: UltraFitApp) {
        DaggerAppComponent.builder().application(ultraFitApp)
                .build().inject(ultraFitApp)

        ultraFitApp.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }


        })
    }

    private fun handleActivity(activity: Activity) {
        AndroidInjection.inject(activity)
    }
}