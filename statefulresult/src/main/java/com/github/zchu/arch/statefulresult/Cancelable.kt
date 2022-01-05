package com.github.zchu.arch.statefulresult

interface Cancelable {

    val isCanceled: Boolean

    fun cancel()
}