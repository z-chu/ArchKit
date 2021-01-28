package com.github.zchu.arch.mock.android.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class MockFragment : Fragment() {

    private lateinit var tvMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout._fragment_mock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvMessage = view.findViewById(R.id.tv_message)
        val stringBuilder = StringBuilder()
        stringBuilder.append("真正的组件没有被依赖，这是一个虚假的 Fragment.")
        val arguments = arguments
        if (arguments != null) {
            stringBuilder.append("\n")
            stringBuilder.append("arguments:$arguments")
            val message = arguments.getString("message")
            if (message != null) {
                stringBuilder.append("\n")
                stringBuilder.append("message:$message")
            }
        }
        tvMessage.text = stringBuilder.toString()

    }

    companion object {
        fun newInstance(message: String): Fragment {
            val args = Bundle()
            args.putString("message", message)
            val fragment = MockFragment()
            fragment.arguments = args
            return fragment
        }
    }
}