package com.example.juniorproject.network

import android.os.Handler
import android.os.Message
import com.example.juniorproject.common.Constant
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.network.dto.ResponseTotalUserInfo
import com.example.juniorproject.util.LogUtil
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Retrofit 통신 api 모음
 */
class RetrofitOption private constructor(){

    companion object{

        private val TAG = RetrofitOption::class.java.simpleName

        @Volatile private var instance: RetrofitOption? = null

        @JvmStatic
        fun getInstance(): RetrofitOption? {
            try{
                if(instance == null) {
                    instance = RetrofitOption()
                    LogUtil.d(TAG, "RetrofitClient Instance 생성")
                }
            } catch (e:Exception){
                LogUtil.e(TAG, "RetrofitClient Instance 생성 오류 : $e")
            }
            return instance
        }

    }

    private fun getInterface(serverUrl:String) : RetrofitInterface{
        return Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // 코루틴
            .build()
            .create(RetrofitInterface::class.java)
    }

//    fun requestTotalUserInfoCo(): ResponseTotalUserInfo {
//        return getInterface(Constant.SERVER_URL).getDeferredServerData()
//    }
    fun requestTotalUserInfoCo(): Deferred<ResponseTotalUserInfo> {
        return getInterface(Constant.SERVER_URL).getDeferredServerData()
    }

    // 전체 유저의 정보
    fun requestTotalUserInfo(handler:Handler){
        try{
            getInterface(Constant.SERVER_URL).getServerData().enqueue(object :
                Callback<ResponseTotalUserInfo> {
                override fun onFailure(call: Call<ResponseTotalUserInfo>, t: Throwable) {
                    LogUtil.e(TAG, "통신 실패 : $t")
                }
                override fun onResponse(call: Call<ResponseTotalUserInfo>, response: Response<ResponseTotalUserInfo>) {
                    try{
                        LogUtil.d(TAG, "통신 성공 : " + response.body())
                        val msg = Message()
                        msg.what = RetrofitApiId.RESPONSE_TOTAL_USER_INFO
                        msg.obj = response.body()!!.info
                        val resHandler:Handler = handler
                        resHandler.sendMessage(msg)
                    }catch (e:Exception){
                        LogUtil.e(TAG, "통신 리턴 성공 후 로직 오류 : $e")
                    }
                }
            })
        }catch (e:Exception){

        }
    }

}

//class ListManager(private val api : RetrofitOption=RetrofitOption()){
//
//    suspend fun getInfoList(param : Map<String, String>) : Observable<RealmTotalUserInfoModel>{
//        val result = api.requestTotalUserInfoCo(param)
//        return process(param)
//    }
//
//    private fun process(response: Map<String, String>) : RealmTotalUserInfoModel {
//        val list = response
//        return
//    }
//
//}