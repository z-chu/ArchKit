package com.github.zchu.arch.statefulresult

import android.os.Looper
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.safeSetValue(value: T) {
    if (Looper.getMainLooper().thread === Thread.currentThread()) {
        setValue(value)
    } else {
        postValue(value)
    }
}