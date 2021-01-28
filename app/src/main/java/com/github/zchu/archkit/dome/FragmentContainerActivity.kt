package com.github.zchu.archkit.dome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.zchu.arch.mock.android.component.MockFragment

class FragmentContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MockFragment.newInstance("This is a test message"))
            .commit()
    }
}