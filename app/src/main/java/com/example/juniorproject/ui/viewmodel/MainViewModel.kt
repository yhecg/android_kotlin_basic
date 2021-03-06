package com.example.juniorproject.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.juniorproject.db.realm.RealmOption
import com.example.juniorproject.db.realm.RealmRepo
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.db.realm.RealmDao
import com.example.juniorproject.network.RetrofitApiId
import com.example.juniorproject.network.RetrofitOption
import com.example.juniorproject.network.dto.ResponseCommonData
import com.example.juniorproject.util.AesUtil
import com.example.juniorproject.util.LogUtil
import io.realm.*
import kotlinx.coroutines.*

/**
 * View 또는 액티비티 Context에 대한 레퍼런스를 가지면 안됩니다.
 * View는 ViewModel의 reference를 가지지만 ViewModel은 View에 대한 정보가 전혀 없어야 합니다.
 * (ViewModel이 View의 레퍼런스를 가진다면 lifecycle 에 따른 메모리릭이 발생할 수 있는데
 * 그 이유는 ViewModel이 destroy 외의 라이프사이클에서는 메모리에서 해제되지 않기 때문입니다.)
 */
class MainViewModel : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    lateinit var mContext:Context

    var mRealm = RealmOption.getInstance()!!

    var dao = RealmDao(mRealm)
    fun getDataObserved(): LiveData<RealmResults<RealmTotalUserInfoModel>>{
        return dao.getDataObserved()
    }


//    val _items = MutableLiveData<ArrayList<RealmTotalUserInfoModel>>(arrayListOf())
//    val items: LiveData<ArrayList<RealmTotalUserInfoModel>>
//        get() = _items

    // 파일 읽기
    @RequiresApi(Build.VERSION_CODES.M)
    fun fileRead(){
        AesUtil(mContext).readFile()
    }

    // 파일 쓰기
    @RequiresApi(Build.VERSION_CODES.M)
    fun fileWrite(){
        AesUtil(mContext).writeFile()
    }

    override fun onCleared() {
        LogUtil.d(TAG, "onCleared")
        mRealm.close()
        super.onCleared()
    }

    // 코루틴 서버 데이터 받기
    fun coroutineServerData(){
        runBlocking {
            val result = RetrofitOption.getInstance()?.requestTotalUserInfoCo()?.await()
            RealmRepo.setInsert(mRealm, result?.info as ArrayList<ResponseCommonData>)
        }
    }

    // 서버에서 데이터 받기
    fun getServerData(){
        try{
            RetrofitOption.getInstance()?.requestTotalUserInfo(mHandler)
        }catch (e:Exception){
            LogUtil.e(TAG, "api 통신 오류 : $e")
        }
    }

    // 서버에서 받은 데이터 Handler
    private var mHandler = @SuppressLint("HandlerLeak")
    object:Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == RetrofitApiId.RESPONSE_TOTAL_USER_INFO){
                try {
                    RealmRepo.setInsert(mRealm, msg.obj as ArrayList<ResponseCommonData>)
                }catch (e:Exception){
                    LogUtil.e(TAG, "통신 데이터 Response 핸들러 오류 : $e")
                }
            }
        }
    }

    // realm 데이터 삭제
    fun setRealmDataDelete(){
        try {
            RealmRepo.setDelete(mRealm)
//            setListView()
        }catch (e:Exception){
            LogUtil.e(TAG, "realm 데이터 삭제 오류 : $e")
        }
    }

    // 저장된 데이터 불러와서 리스트뷰 연결
//    fun setListView() {
//        try {
//            val dbResult = RealmRepo.getRead(mRealm)
//            _items.value?.clear()
//            val list = _items.value
//            list?.addAll(dbResult!!)
//            _items.value = list
//        } catch (e: Exception) {
//            LogUtil.e(TAG, "리스트뷰 데이터 처리 오류 : $e")
//        }
//    }

    // 액티비티 띄우기
//    var showAct = MutableLiveData<Int>()
//    fun callShowActivity(type:Int){
//        showAct.value = type
//    }



}