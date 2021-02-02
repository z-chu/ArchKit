package com.github.zchu.arch.lifecycle.rx

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


open class RxViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun Disposable.disposeWhenCleared() {
        compositeDisposable.add(this)
    }

    fun Disposable.cancelDisposeWhenCleared() {
        compositeDisposable.delete(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}