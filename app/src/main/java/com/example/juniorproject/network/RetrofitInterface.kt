package com.example.juniorproject.network

import com.example.juniorproject.network.dto.ResponseTotalUserInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * 통신 Retrofit 에서 사용할 인터페이스.
 */
interface RetrofitInterface {

    // 유저들의 전체 정보를 가져온다
    @POST("/main/getData")
    fun getServerData() : Call<ResponseTotalUserInfo>

    // @return Deferred<T> 코루틴의 지연된 콜백 인터페이스 반환, T 는 주고 받을 데이터 구조
    @POST("/main/getData")
//    fun getDeferredServerData() : ResponseTotalUserInfo
    fun getDeferredServerData() : Deferred<ResponseTotalUserInfo>

}