package com.github.zchu.archkit.dome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.github.zchu.arch.lifecycle.rx.bindLifecycle
import com.github.zchu.arch.mock.android.component.MockActivity
import com.github.zchu.arch.statefulresult.StatefulResult
import com.github.zchu.arch.statefulresult.observeStateful
import com.github.zchu.statefulresult.rx3.subscribeTo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit

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
            Observable
                .just("成功拉！啦啦啦！")
                .delay(3, TimeUnit.SECONDS)
                .take(1)
                .map {
                    if (System.currentTimeMillis() % 2 == 0L) {
                        throw  RuntimeException()
                    } else {
                        it
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTo(mutableLiveResult)
                .bindLifecycle(this)
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