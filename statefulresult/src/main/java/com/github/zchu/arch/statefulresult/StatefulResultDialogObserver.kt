package com.github.zchu.arch.statefulresult

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

abstract class StatefulResultDialogObserver<T>(
    protected val fragmentManager: FragmentManager
) : StatefulResultObserver<T>() {

    protected open val fragmentTag: String = this::class.java.name
    private var lifecycleEventObserver: LifecycleEventObserver? = null

    override fun onLoading(canceler: (() -> Unit)?) {
        super.onLoading(canceler)
        var findFragmentByTag = fragmentManager.findFragmentByTag(fragmentTag)
        if (findFragmentByTag == null) {
            val createDialogFragment = createDialogFragment()
            findFragmentByTag = createDialogFragment
        }
        if (findFragmentByTag is DialogFragment && !findFragmentByTag.isAdded) {
            if (!fragmentManager.isDestroyed && !fragmentManager.isStateSaved) {
                lifecycleEventObserver?.let {
                    findFragmentByTag.lifecycle.removeObserver(it)
                }
                val observer = object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            canceler?.invoke()
                            source.lifecycle.removeObserver(this)
                        }
                    }

                }
                lifecycleEventObserver = observer
                findFragmentByTag.lifecycle.addObserver(observer)
                try {
                    findFragmentByTag.show(fragmentManager, fragmentTag)
                } catch (ignore: Exception) {
                }
            }
        }
    }

    override fun onFailure(throwable: Throwable) {
        super.onFailure(throwable)
        dismissDialogFragment()
    }


    override fun onSuccess(value: T) {
        super.onSuccess(value)
        dismissDialogFragment()
    }

    abstract fun createDialogFragment(): DialogFragment

    protected val dialogFragment: DialogFragment?
        get() = fragmentManager.findFragmentByTag(fragmentTag) as? DialogFragment

    protected fun dismissDialogFragment() {
        val findFragmentByTag = fragmentManager.findFragmentByTag(fragmentTag)
        if (findFragmentByTag != null && findFragmentByTag is DialogFragment) {
            lifecycleEventObserver?.let {
                findFragmentByTag.lifecycle.removeObserver(it)
            }
            try {
                findFragmentByTag.dismiss()
            } catch (ignore: Exception) {
                findFragmentByTag.dismissAllowingStateLoss()
            }
        }

    }

}
