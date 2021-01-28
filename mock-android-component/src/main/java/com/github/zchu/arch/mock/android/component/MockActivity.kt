package com.github.zchu.arch.mock.android.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class MockActivity : Activity() {

    private lateinit var tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._activity_mock)
        tvMessage = findViewById(R.id.tv_message)
        val stringBuilder = StringBuilder()
        stringBuilder.append("真正的组件没有被依赖，这是一个虚假的 Activity.")
        val intent = intent
        if (intent != null) {
            stringBuilder.append("\n")
            stringBuilder.append("Intent:$intent")
        }
        val message = intent.getStringExtra("message")
        if (message != null) {
            stringBuilder.append("\n")
            stringBuilder.append("message:$message")
        }
        tvMessage.text = stringBuilder.toString()
    }

    companion object {

        fun newIntent(context: Context, message: String? = null): Intent {
            val intent = Intent(context, MockActivity::class.java)
            if (message != null) {
                intent.putExtra("message", message)
            }
            return intent
        }
    }
}