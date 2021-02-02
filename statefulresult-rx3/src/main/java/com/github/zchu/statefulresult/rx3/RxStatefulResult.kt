package com.github.zchu.statefulresult.rx3

import androidx.lifecycle.MutableLiveData
import com.github.zchu.arch.statefulresult.Failure
import com.github.zchu.arch.statefulresult.Loading
import com.github.zchu.arch.statefulresult.StatefulResult
import com.github.zchu.arch.statefulresult.Success
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable


private val onSuccessStub: (Any) -> Unit = {}
private val onFailureStub: (Throwable) -> Unit = {}

@CheckReturnValue
fun <T : Any> Observable<T>.subscribeTo(
    mutableLiveData: MutableLiveData<StatefulResult<T>>,
    onSuccessBefore: (T) -> Unit = onSuccessStub,
    onFailureBefore: (Throwable) -> Unit = onFailureStub,
    onSuccessAfter: (T) -> Unit = onSuccessStub,
    onFailureAfter: (Throwable) -> Unit = onFailureStub
): Disposable {
    val loading = Loading<T>()
    mutableLiveData.safeSetValue(loading)
    val disposable = subscribe(
        {
            onSuccessBefore.invoke(it)
            mutableLiveData.safeSetValue(Success(it))
            onSuccessAfter.invoke(it)

        },
        {
            onFailureBefore.invoke(it)
            mutableLiveData.safeSetValue(Failure(it))
            onFailureAfter.invoke(it)
        }
    )
    loading.bindCancelerForRx(disposable, mutableLiveData)
    return disposable
}

@CheckReturnValue
fun <T : Any> Flowable<T>.subscribeTo(
    mutableLiveData: MutableLiveData<StatefulResult<T>>,
    onSuccessBefore: (T) -> Unit = onSuccessStub,
    onFailureBefore: (Throwable) -> Unit = onFailureStub,
    onSuccessAfter: (T) -> Unit = onSuccessStub,
    onFailureAfter: (Throwable) -> Unit = onFailureStub
): Disposable {
    val loading = Loading<T>()
    mutableLiveData.safeSetValue(loading)
    val disposable = this.subscribe(
        {
            onSuccessBefore.invoke(it)
            mutableLiveData.safeSetValue(Success(it))
            onSuccessAfter.invoke(it)

        },
        {
            onFailureBefore.invoke(it)
            mutableLiveData.safeSetValue(Failure(it))
            onFailureAfter.invoke(it)
        }
    )
    loading.bindCancelerForRx(disposable, mutableLiveData)
    return disposable
}