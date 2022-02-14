package com.github.zchu.arch.lifecycle.rx

import android.app.Dialog
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

internal class LoadingDialogHandler(dialog: Dialog) {


    private val handler: MyHandler = MyHandler(dialog)
    private var mDialog: Dialog? = dialog

    fun showDialog() {
        handler.sendEmptyMessage(MyHandler.SHOW_PROGRESS_DIALOG)
        mDialog = null
    }

    fun dismissDialog() {
        handler.sendEmptyMessage(MyHandler.DISMISS_PROGRESS_DIALOG)
    }

    private class MyHandler internal constructor(dialog: Dialog) : Handler() {

        private val dialogWeakReference: WeakReference<Dialog> = WeakReference(dialog)

        override fun handleMessage(msg: Message) {
            val dialog = dialogWeakReference.get()
            when (msg.what) {
                SHOW_PROGRESS_DIALOG -> if (dialog != null && !dialog.isShowing) {
                    try {
                        dialog.show()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
                DISMISS_PROGRESS_DIALOG -> if (dialog != null && dialog.isShowing) {
                    try {
                        dialog.dismiss()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
            }
        }

        companion object {
            const val SHOW_PROGRESS_DIALOG = 1
            const val DISMISS_PROGRESS_DIALOG = 2
        }
    }
}