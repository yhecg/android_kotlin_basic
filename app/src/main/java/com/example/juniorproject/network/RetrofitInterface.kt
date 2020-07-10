package com.example.juniorproject.network

import com.example.juniorproject.network.dto.ResponseTotalUserInfo
import retrofit2.Call
import retrofit2.http.POST

/**
 * 통신 Retrofit 에서 사용할 인터페이스.
 */
interface RetrofitInterface {

    // 유저들의 전체 정보를 가져온다
    @POST("/main/getData")
    fun getServerData() : Call<ResponseTotalUserInfo>

}