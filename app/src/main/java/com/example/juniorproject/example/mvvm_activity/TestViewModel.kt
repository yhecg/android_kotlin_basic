package com.example.juniorproject.example.mvvm_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {

    private val TAG = TestViewModel::class.java.simpleName

    var counter = MutableLiveData<Int>()

    init {
        counter.value = 0
    }

    fun setPlus(){
        counter.value = counter.value?.plus(1)
    }

    fun setMinus(){
        counter.value = counter.value?.minus(1)
    }

}