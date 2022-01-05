package com.github.zchu.statefulresult.rx3

import androidx.lifecycle.MutableLiveData
import com.github.zchu.arch.statefulresult.Failure
import com.github.zchu.arch.statefulresult.Loading
import com.github.zchu.arch.statefulresult.StatefulResult
import com.github.zchu.arch.statefulresult.safeSetValue
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.CancellationException

fun <T> Loading<T>.bindCancelerForRx(
    disposable: Disposable,
    mutableLiveData: MutableLiveData<StatefulResult<T>>
): Loading<T> {
    this.canceler = {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        mutableLiveData.safeSetValue(Failure(CancellationException()))
    }
    return this
}