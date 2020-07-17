package com.example.juniorproject.ui

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.ui.adapter.ListAdapter
import com.example.juniorproject.ui.adapter.TotalUserInfoListAdapter

object BindingAdapters {

    @BindingAdapter("bindInit")
    @JvmStatic
    fun RecyclerView.bindInit(items: ArrayList<RealmTotalUserInfoModel>){

//        val adapter = adapter as TotalUserInfoListAdapter
//        adapter.items = items
//        adapter.notifyDataSetChanged()

        val adapter = adapter as ListAdapter
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}