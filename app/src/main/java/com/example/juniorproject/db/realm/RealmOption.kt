package com.example.juniorproject.db.realm

import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.network.dto.ResponseCommonData
import com.example.juniorproject.util.LogUtil
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Realm 시작하기 위한 인스턴스
 */
class RealmOption private constructor(){

    companion object {

        private val TAG = RealmOption::class.java.simpleName

        @Volatile private var instance: Realm? = null

        @JvmStatic
        fun getInstance(): Realm? {
            try {
                if (instance == null) {
                    val conf : RealmConfiguration = RealmConfiguration.Builder()
                        .name("default.realm")
                        .deleteRealmIfMigrationNeeded()
                        .build()
                    instance = Realm.getInstance(conf)
                    LogUtil.d(TAG, "realm instance 생성")
                }
            } catch (e: Exception) {
                LogUtil.e(TAG, "realm 설정 오류 : $e")
            }
            return instance
        }
    }

}