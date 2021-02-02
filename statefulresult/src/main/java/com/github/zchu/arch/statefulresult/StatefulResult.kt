package com.github.zchu.arch.statefulresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class StatefulResult<T> {
    var tag: Any? = null
}

class Loading<T>(var canceler: (() -> Unit)? = null) : StatefulResult<T>()

class Success<T>(val value: T) : StatefulResult<T>()

class Failure<T>(val throwable: Throwable) : StatefulResult<T>() {
    constructor() : this(FailedStateException())
    constructor(message: String) : this(FailedStateException(message))
}

inline fun <T> StatefulResult<T>.switch(
    onLoading: (Loading<T>) -> Unit = { _ -> },
    onSuccess: (Success<T>) -> Unit = { _ -> },
    onFailure: (Failure<T>) -> Unit = { _ -> }
) {
    when (this) {
        is Loading<T> -> onLoading.invoke(this)
        is Success<T> -> onSuccess.invoke(this)
        is Failure<T> -> onFailure.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnLoading(
    onLoading: (Loading<T>) -> Unit = { _ -> }
) {
    if (this is Loading<T>) {
        onLoading.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnSuccess(
    onSuccess: (Success<T>) -> Unit = { _ -> }
) {
    if (this is Success<T>) {
        onSuccess.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnFailure(
    onFailure: (Failure<T>) -> Unit = { _ -> }
) {
    if (this is Failure<T>) {
        onFailure.invoke(this)
    }
}

typealias LiveResult<T> = LiveData<StatefulResult<T>>

typealias MutableLiveResult<T> = MutableLiveData<StatefulResult<T>>

fun StatefulResult<*>.isLoading() = this is Loading

fun StatefulResult<*>.isFailure() = this is Failure

fun StatefulResult<*>.isSuccess() = this is Success

fun <T> LiveData<StatefulResult<T>>.isLoading() = value is Loading

fun <T> LiveData<StatefulResult<T>>.isFailure() = value is Failure

fun <T> LiveData<StatefulResult<T>>.isSuccess() = value is Success

fun <T> LiveData<StatefulResult<T>>.isFailureOrNone() = value == null || value is Failure

inline fun <reified T> LiveData<StatefulResult<T>>.getSuccessFromTagAndValue(): T? {
    val value = value
    if (value is Success) {
        return value.value
    }
    val tag = value?.tag
    if (tag is T) {
        return tag
    }
    return null
}


