package com.github.zchu.archkit.dome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.github.zchu.arch.mock.android.component.MockActivity
import com.github.zchu.arch.statefulresult.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {
    private val mTvStateful by lazy { findViewById<TextView>(R.id.tv_stateful) }

    private val autoInstallString: String by inject(named("auto_install_string"))

    private val mutableLiveResult: MutableLiveData<StatefulResult<String>> =
        MutableLiveData<StatefulResult<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv).text = autoInstallString
        findViewById<View>(R.id.btn_skip_activity).setOnClickListener {
            startActivity(MockActivity.newIntent(this, "This is a test message"))
        }
        findViewById<View>(R.id.btn_skip_fragment).setOnClickListener {
            startActivity(Intent(this, FragmentContainerActivity::class.java))
        }

        findViewById<View>(R.id.btn_stateful).setOnClickListener {
            if (mutableLiveResult.isFailureOrNone()) {
                mutableLiveResult.value = Loading()
                object : Thread() {
                    override fun run() {
                        super.run()
                        sleep(3000)
                        val currentTimeMillis = System.currentTimeMillis()
                        val l = currentTimeMillis % 2
                        if (l == 0L) {
                            mutableLiveResult.postValue(Failure())
                        } else {
                            mutableLiveResult.postValue(Success("成功拉！啦啦啦！"))
                        }
                    }
                }.start()
            }
        }
        mutableLiveResult
            .observeStateful(
                this,
                onLoading = {
                    mTvStateful.text = "加载中..."
                },
                onFailure = {
                    mTvStateful.text = "加载失败"
                },
                onSuccess = {
                    mTvStateful.text = it
                }
            )
    }
}