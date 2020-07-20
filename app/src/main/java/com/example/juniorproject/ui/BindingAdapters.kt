package com.example.juniorproject.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.ui.adapter.RealmRVAdapter
import com.example.juniorproject.ui.adapter.TotalUserInfoListAdapter
import io.realm.RealmResults

object BindingAdapters {

    @BindingAdapter("bindInit")
    @JvmStatic
    fun RecyclerView.bindInit(items: ArrayList<RealmTotalUserInfoModel>){
        val adapter = adapter as TotalUserInfoListAdapter
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}