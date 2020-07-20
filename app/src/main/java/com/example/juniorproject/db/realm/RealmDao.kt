package com.example.juniorproject.db.realm

import androidx.lifecycle.LiveData
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

class RealmDao(val realm:Realm) {

    private fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)

    fun getDataObserved() : LiveData<RealmResults<RealmTotalUserInfoModel>>{
        return realm.where(RealmTotalUserInfoModel::class.java).findAll().asLiveData()
    }

}