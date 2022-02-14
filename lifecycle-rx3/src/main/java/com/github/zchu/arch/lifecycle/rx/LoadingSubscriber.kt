package com.github.zchu.arch.lifecycle.rx

import android.app.Dialog
import androidx.annotation.CallSuper
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


abstract class LoadingSubscriber<T> : Observer<T> {

    private lateinit var progressDialogHandler: LoadingDialogHandler

    @Volatile
    protected var disposable: Disposable? = null
        private set

    @Volatile
    protected var dialog: Dialog? = null
        private set

    @CallSuper
    override fun onSubscribe(disposable: Disposable) {
        this.disposable = disposable
        val createProgressDialog = createProgressDialog(disposable)
        progressDialogHandler = LoadingDialogHandler(createProgressDialog)
        progressDialogHandler.showDialog()
        dialog = createProgressDialog
    }

    @CallSuper
    override fun onComplete() {
        progressDialogHandler.dismissDialog()
    }

    @CallSuper
    override fun onError(e: Throwable) {
        progressDialogHandler.dismissDialog()
    }

    abstract fun createProgressDialog(disposable: Disposable): Dialog

}
