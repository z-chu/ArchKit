package com.github.zchu.arch.statefulresult

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class StatefulResultObserver<T> : Observer<StatefulResult<T>> {

    final override fun onChanged(t: StatefulResult<T>?) {
        when (t) {
            is Loading<T> -> onLoading(t.canceler)
            is Success<T> -> onSuccess(t.value)
            is Failure<T> -> onFailure(t.throwable)
        }
    }

    open fun onLoading(canceler: (() -> Unit)?) {
    }

    open fun onSuccess(value: T) {
    }

    open fun onFailure(throwable: Throwable) {
        loggerOnFailure?.invoke(throwable)
    }

    companion object {
        var loggerOnFailure: ((Throwable) -> Unit)? = {
            Log.e("StatefulResultObserver", it.message ?: "${it} onFailure: message is null")
        }

    }

}

private val onLoadingStub: (canceler: (() -> Unit)?) -> Unit = {}
private val onFailureStub: (Throwable) -> Unit = {}
private val onSuccessStub: (Any) -> Unit = {}


private class BridgeStatefulResultObserver<T>(
    private val onLoading: (canceler: (() -> Unit)?) -> Unit,
    private val onFailure: (throwable: Throwable) -> Unit,
    private val onSuccess: (value: T) -> Unit
) : StatefulResultObserver<T>() {


    override fun onLoading(canceler: (() -> Unit)?) {
        super.onLoading(canceler)
        onLoading.invoke(canceler)
    }

    override fun onSuccess(value: T) {
        super.onSuccess(value)
        onSuccess.invoke(value)
    }

    override fun onFailure(throwable: Throwable) {
        super.onFailure(throwable)
        onFailure.invoke(throwable)
    }
}

@JvmOverloads
fun <T : Any> LiveData<StatefulResult<T>>.observeStateful(
    owner: LifecycleOwner,
    onLoading: (canceler: (() -> Unit)?) -> Unit = onLoadingStub,
    onFailure: (throwable: Throwable) -> Unit = onFailureStub,
    onSuccess: (value: T) -> Unit = onSuccessStub
) {
    this.observe(owner, BridgeStatefulResultObserver(onLoading, onFailure, onSuccess))
}