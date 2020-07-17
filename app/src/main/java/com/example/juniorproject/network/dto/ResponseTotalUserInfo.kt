package com.example.juniorproject.network.dto

/**
 * 서버에서 받는 전체 사용자 정보
 */

// 전체 데이터
data class ResponseTotalUserInfo (
    val info:ArrayList<ResponseCommonData>
)

// 공통으로 사용되는 부분
open class ResponseCommonData (
    val type: String,
    val error: ArrayList<String>,
    val estimated_data: ArrayList<String>,
    val original_data: ArrayList<String>,
    val reliability: ArrayList<String>
)
