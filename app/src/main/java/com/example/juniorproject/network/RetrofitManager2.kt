package com.example.juniorproject.network

import com.example.juniorproject.common.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit 을 사용하기 위한 셋팅
 */
object RetrofitManager2 {

    private val TAG = RetrofitManager2::class.java.simpleName

    val retrofitInterface: RetrofitInterface = Retrofit.Builder()
        .baseUrl(Constant.SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitInterface::class.java)

}