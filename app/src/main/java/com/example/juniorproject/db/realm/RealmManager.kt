package com.example.juniorproject.db.realm

import com.example.juniorproject.util.LogUtil
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Realm 시작하기 위한 인스턴스
 */
object RealmManager {

    private val TAG = RealmManager::class.java.simpleName

    private var instance : Realm? = null

    fun getInstance():Realm?{
        try{
            if(instance == null){
                val conf : RealmConfiguration = RealmConfiguration.Builder()
                    .name("default.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build()
                instance = Realm.getInstance(conf)
                LogUtil.d(TAG, "realm instance 생성")
            }
        } catch (e:Exception){
            LogUtil.e(TAG, "realm 설정 오류 : $e")
        }
        return instance
    }

//    companion object{
//
//        private val TAG = RealmManager::class.java.simpleName
//
//        private var instance: Realm? = null
//
//        @Synchronized
//        @JvmStatic
//        fun getInstance(type:String, module:Any): Realm? {
////        fun getInstance(): Realm? {
//            try{
//                if(instance == null) {
//                    val conf:RealmConfiguration = RealmConfiguration.Builder()
////                        .encryptionKey()
////                        .schemaVersion()
//                        .modules(module)
//                        .name("$type.realm")
//                        .build()
//                    instance = Realm.getInstance(conf)
////                    instance = Realm.getDefaultInstance()
//                    LogUtil.d(TAG, "realm instance 생성")
//                }
//            } catch (e:Exception){
//                LogUtil.e(TAG, "realm 설정 오류 : $e")
//            }
//            return instance
//        }
//
//    }

}