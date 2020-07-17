package com.example.juniorproject.db.realm

import com.example.juniorproject.db.realm.model.TotalUserInfoDao
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

class RealmDao {

    fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
    fun Realm.totalUserInfoDao() : TotalUserInfoDao = TotalUserInfoDao()

}