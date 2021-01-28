package com.github.zchu.archkit.dome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.zchu.arch.mock.android.component.MockActivity
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {

    private val autoInstallString:String by inject(named("auto_install_string"))

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
    }
}