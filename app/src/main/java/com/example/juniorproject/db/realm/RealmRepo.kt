package com.example.juniorproject.db.realm

import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.network.dto.ResponseCommonData
import com.example.juniorproject.util.LogUtil
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

object RealmRepo {

    private val TAG = RealmRepo::class.java.simpleName

    // Realm PrimaryKey Index 구하기
    private fun getPrimaryKeyIndex(realm:Realm):Int{
        val idx = realm.where(RealmTotalUserInfoModel::class.java).max("idx")
        return if(idx == null){
            1
        }else{
            idx.toInt()+1
        }
    }

    // 데이터 보기
    fun getRead(realm:Realm): RealmResults<RealmTotalUserInfoModel>? {
        return realm.where(RealmTotalUserInfoModel::class.java)!!.findAll()
    }

//    // 데이터 보기
//    fun getRead(realm:Realm): MutableList<RealmTotalUserInfoModel>? {
//        val result: RealmResults<RealmTotalUserInfoModel> =
//            realm.where(RealmTotalUserInfoModel::class.java)!!.findAll()
//        val dbList: MutableList<RealmTotalUserInfoModel>? = realm.copyFromRealm(result)
//        LogUtil.d(TAG, "realm 데이터 json : " + result.asJSON())
//        return dbList
//    }

    // 데이터 등록
    fun setInsert(realm: Realm, infoList:ArrayList<ResponseCommonData>) {
        for(i in 0 until infoList.size){
            val count = getPrimaryKeyIndex(realm)
            realm.beginTransaction()
            val model: RealmTotalUserInfoModel = realm.createObject(
                RealmTotalUserInfoModel::class.java, count)
            model.type = infoList[i].type
            model.error = infoList[i].error.toString()
            model.estimated_data = infoList[i].estimated_data.toString()
            model.original_data = infoList[i].original_data.toString()
            model.reliability = infoList[i].reliability.toString()
            realm.commitTransaction()
        }
    }

    // 데이터 삭제
    fun setDelete(realm:Realm){
        realm.beginTransaction()
        val result: RealmResults<RealmTotalUserInfoModel> =
            realm.where(RealmTotalUserInfoModel::class.java)!!.findAll()
        result.deleteAllFromRealm()
        realm.commitTransaction()
    }

    // 데이터 수정
    fun setUpdate(realm:Realm, index: Int?){
        realm.beginTransaction()
        val result: RealmTotalUserInfoModel? =
            realm.where(RealmTotalUserInfoModel::class.java)?.equalTo("idx", index)?.findFirst()
        result?.type = "수정완료"
        realm.commitTransaction()
    }

}