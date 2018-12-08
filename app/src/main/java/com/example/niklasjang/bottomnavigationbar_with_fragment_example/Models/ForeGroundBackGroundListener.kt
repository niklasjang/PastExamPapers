package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log

class ForegroundBackgroundListener : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun appStarted() {
        Log.d("ForegroundBackgroundListener","App is on foreground")
        //Todo visibility: gone
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun appStopped() {
        Log.d("ForegroundBackgroundListener","App is on background")
        //Todo visibility: visible
    }
}