package com.example.juniorproject.network

import com.example.juniorproject.common.Constant
import com.example.juniorproject.util.LogUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.Exception

/**
 * Retrofit 을 사용하기 위한 셋팅
 */
class RetrofitManager {

    companion object{

        private val TAG = RetrofitManager::class.java.simpleName

        private var retrofitInterface: RetrofitInterface? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): RetrofitInterface? {
            try{
                if(retrofitInterface == null) {
                    retrofitInterface = Retrofit.Builder()
                        .baseUrl(Constant.SERVER_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RetrofitInterface::class.java)
                    LogUtil.d(TAG, "Retrofit Instance 생성")
                }
            } catch (e:Exception){
                LogUtil.e(TAG, "통신 설정 오류 : $e")
            }
            return retrofitInterface
        }

    }

}