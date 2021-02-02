package com.github.zchu.statefulresult.rx3

import android.os.Looper
import androidx.lifecycle.MutableLiveData

internal fun <T> MutableLiveData<T>.safeSetValue(value: T) {
    if (Looper.getMainLooper().thread === Thread.currentThread()) {
        setValue(value)
    } else {
        postValue(value)
    }
}