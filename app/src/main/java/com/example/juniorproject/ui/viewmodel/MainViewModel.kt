package com.example.juniorproject.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.juniorproject.example.mvvm_fragment.VmSharedActivity

class MainViewModel : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    init {

    }

    fun startMvvmFrag(context: Context){
        val intent: Intent = Intent(context, VmSharedActivity::class.java)
        context.startActivity(intent)
    }

}