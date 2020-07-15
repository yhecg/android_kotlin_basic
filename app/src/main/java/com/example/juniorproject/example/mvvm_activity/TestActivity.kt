package com.example.juniorproject.example.mvvm_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.juniorproject.R
import com.example.juniorproject.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val TAG = TestActivity::class.java.simpleName

    private lateinit var binding: ActivityTestBinding

    private lateinit var viewModel: TestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)
            .apply {

                lifecycleOwner = this@TestActivity

                vm = viewModel

        }

    }


}