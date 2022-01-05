package com.github.zchu.statefulresult.call

import androidx.lifecycle.MutableLiveData
import com.github.zchu.arch.statefulresult.Cancelable
import com.github.zchu.arch.statefulresult.Failure
import com.github.zchu.arch.statefulresult.Loading
import com.github.zchu.arch.statefulresult.StatefulResult
import com.github.zchu.arch.statefulresult.Success
import com.github.zchu.arch.statefulresult.bindCanceler
import com.github.zchu.arch.statefulresult.safeSetValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


private val onSuccessStub: (Any) -> Unit = {}
private val onFailureStub: (Throwable) -> Unit = {}

fun <T : Any> Call<T>.enqueueTo(
    mutableLiveData: MutableLiveData<StatefulResult<T>>,
    tag: Any? = null,
    onSuccessBefore: (T) -> Unit = onSuccessStub,
    onFailureBefore: (Throwable) -> Unit = onFailureStub,
    onSuccessAfter: (T) -> Unit = onSuccessStub,
    onFailureAfter: (Throwable) -> Unit = onFailureStub
): Cancelable {
    val cancelable = object : Cancelable {
        override val isCanceled: Boolean
            get() = this@enqueueTo.isCanceled

        override fun cancel() {
            this@enqueueTo.cancel()
        }

    }
    val loading = Loading<T>()
    loading.tag = tag
    loading.bindCanceler(cancelable, mutableLiveData)
    mutableLiveData.safeSetValue(loading)
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val body = response.body()!!
            onSuccessBefore.invoke(body)
            val success = Success(body)
            success.tag = tag
            mutableLiveData.safeSetValue(success)
            onSuccessAfter.invoke(body)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailureBefore.invoke(t)
            val failure = Failure<T>(t)
            failure.tag = tag
            mutableLiveData.safeSetValue(failure)
            onFailureAfter.invoke(t)
        }

    })
    return cancelable
}


fun <T : Any> Call<T>.enqueueResponseTo(
    mutableLiveData: MutableLiveData<StatefulResult<Response<T>>>,
    tag: Any? = null,
    onSuccessBefore: (Response<T>) -> Unit = onSuccessStub,
    onFailureBefore: (Throwable) -> Unit = onFailureStub,
    onSuccessAfter: (Response<T>) -> Unit = onSuccessStub,
    onFailureAfter: (Throwable) -> Unit = onFailureStub
): Cancelable {
    val cancelable = object : Cancelable {
        override val isCanceled: Boolean
            get() = this@enqueueResponseTo.isCanceled

        override fun cancel() {
            this@enqueueResponseTo.cancel()
        }

    }
    val loading = Loading<Response<T>>()
    loading.tag = tag
    loading.bindCanceler(cancelable, mutableLiveData)
    mutableLiveData.safeSetValue(loading)
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onSuccessBefore.invoke(response)
            val success = Success(response)
            success.tag = tag
            mutableLiveData.safeSetValue(success)
            onSuccessAfter.invoke(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailureBefore.invoke(t)
            val failure = Failure<Response<T>>(t)
            failure.tag = tag
            mutableLiveData.safeSetValue(failure)
            onFailureAfter.invoke(t)
        }

    })
    return cancelable
}


fun okhttp3.Call.enqueueTo(
    mutableLiveData: MutableLiveData<StatefulResult<okhttp3.Response>>,
    tag: Any? = null,
    onSuccessBefore: (okhttp3.Response) -> Unit = onSuccessStub,
    onFailureBefore: (Throwable) -> Unit = onFailureStub,
    onSuccessAfter: (okhttp3.Response) -> Unit = onSuccessStub,
    onFailureAfter: (Throwable) -> Unit = onFailureStub
): Cancelable {
    val cancelable = object : Cancelable {
        override val isCanceled: Boolean
            get() = this@enqueueTo.isCanceled

        override fun cancel() {
            this@enqueueTo.cancel()
        }
    }
    val loading = Loading<okhttp3.Response>()
    loading.tag = tag
    loading.bindCanceler(cancelable, mutableLiveData)
    mutableLiveData.safeSetValue(loading)
    enqueue(object : okhttp3.Callback {
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            onSuccessBefore.invoke(response)
            val success = Success(response)
            success.tag = tag
            mutableLiveData.safeSetValue(success)
            onSuccessAfter.invoke(response)
        }

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            onFailureBefore.invoke(e)
            val failure = Failure<okhttp3.Response>(e)
            failure.tag = tag
            mutableLiveData.safeSetValue(failure)
            onFailureAfter.invoke(e)
        }
    })
    return cancelable
}