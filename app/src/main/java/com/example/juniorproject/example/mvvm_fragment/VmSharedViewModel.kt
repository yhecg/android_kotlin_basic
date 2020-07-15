package com.example.juniorproject.example.mvvm_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VmSharedViewModel : ViewModel() {

    var progress = MutableLiveData<Int>()

}