package com.github.zchu.arch.statefulresult

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.CancellationException

fun <T> Loading<T>.bindCanceler(
    cancelable: Cancelable,
    mutableLiveData: MutableLiveData<StatefulResult<T>>
): Loading<T> {
    this.canceler = {
        if (!cancelable.isCanceled) {
            cancelable.cancel()
        }
        mutableLiveData.safeSetValue(Failure(CancellationException()))
    }
    return this
}