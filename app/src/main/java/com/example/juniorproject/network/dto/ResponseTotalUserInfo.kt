package com.example.juniorproject.network.dto

/**
 * 서버에서 받는 전체 사용자 정보
 */

// 전체 데이터
data class ResponseTotalUserInfo (
    val info:MutableList<ResponseCommonData>
)

// 공통으로 사용되는 부분
open class ResponseCommonData (
    val type: String,
    val error: MutableList<String>,
    val estimated_data: MutableList<String>,
    val original_data: MutableList<String>,
    val reliability: MutableList<String>
)
