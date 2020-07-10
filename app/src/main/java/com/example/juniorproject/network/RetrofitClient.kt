package com.example.juniorproject.network

import android.os.Handler
import android.os.Message
import com.example.juniorproject.network.dto.ResponseTotalUserInfo
import com.example.juniorproject.util.LogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Retrofit 통신 api 모음
 */
class RetrofitClient {

    companion object{

        private val TAG = RetrofitClient::class.java.simpleName

        private var instance: RetrofitClient? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): RetrofitClient? {
            try{
                if(instance == null) {
                    instance = RetrofitClient()
                    LogUtil.d(TAG, "RetrofitClient Instance 생성")
                }
            } catch (e:Exception){
                LogUtil.e(TAG, "RetrofitClient Instance 생성 오류 : $e")
            }
            return instance
        }

    }

    // 전체 유저의 정보
    fun requestTotalUserInfo(handler:Handler){
        try{
            RetrofitManager.getInstance()?.getServerData()?.enqueue(object :
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