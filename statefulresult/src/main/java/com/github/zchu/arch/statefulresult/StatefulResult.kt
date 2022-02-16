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
    onFailure: (Failure<T>) -> Unit = { _ -> },
) {
    when (this) {
        is Loading<T> -> onLoading.invoke(this)
        is Success<T> -> onSuccess.invoke(this)
        is Failure<T> -> onFailure.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnLoading(
    onLoading: (Loading<T>) -> Unit = { _ -> },
) {
    if (this is Loading<T>) {
        onLoading.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnSuccess(
    onSuccess: (Success<T>) -> Unit = { _ -> },
) {
    if (this is Success<T>) {
        onSuccess.invoke(this)
    }
}

inline fun <T> StatefulResult<T>.doOnFailure(
    onFailure: (Failure<T>) -> Unit = { _ -> },
) {
    if (this is Failure<T>) {
        onFailure.invoke(this)
    }
}

typealias LiveResult<T> = LiveData<StatefulResult<T>>

typealias MutableLiveResult<T> = MutableLiveData<StatefulResult<T>>

val StatefulResult<*>.isLoading get() = this is Loading

val StatefulResult<*>.isFailure get() = this is Failure

val StatefulResult<*>.isSuccess get() = this is Success

val <T> LiveData<StatefulResult<T>>.isLoading get() = value is Loading

val <T> LiveData<StatefulResult<T>>.isFailure get() = value is Failure

val <T> LiveData<StatefulResult<T>>.isSuccess get() = value is Success

val <T> LiveData<StatefulResult<T>>.isFailureOrNone: Boolean
    get() {
        val localValue = value
        return localValue == null || localValue is Failure
    }

inline fun <T> LiveData<StatefulResult<T>>.runOnFailureOrNone(block: () -> Unit) {
    if (isFailureOrNone) {
        block.invoke()
    }
}

fun LiveData<StatefulResult<*>>.cancelWhenCurrentIsLoading() {
    val localValue = value
    if (localValue is Loading) {
        localValue.canceler?.invoke()
    }
}

val <T> LiveData<StatefulResult<T>>.successValue: T?
    get() {
        return value?.successValue
    }

val <T> StatefulResult<T>.successValue: T?
    get() {
        if (this is Success) {
            return this.value
        }
        return null
    }


inline fun <reified T> LiveData<StatefulResult<T>>.getSuccessValueCheckTag(): T? {
    return value?.getSuccessValueCheckTag()
}

inline fun <reified T> StatefulResult<T>.getSuccessValueCheckTag(): T? {
    if (this is Success) {
        return this.value
    }
    val tag = this.tag
    if (tag is T) {
        return tag
    }
    return null
}


val <T> LiveData<StatefulResult<T>>.resultTag: Any?
    get() {
        return value?.tag
    }
