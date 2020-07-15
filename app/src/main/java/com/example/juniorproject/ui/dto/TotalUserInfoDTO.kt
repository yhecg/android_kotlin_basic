package com.example.juniorproject.ui.dto

/**
 * 리스트뷰에 보여줄 item
 * type : 성별, 연령대 등
 * error :
 * estimated_data :
 * original_data :
 * reliability :
 */
data class TotalUserInfoDTO (
    var idx: Int = 0,
    var type: String? = "",
    val error: String? = "",
    val estimated_data: String? = "",
    val original_data: String? = "",
    val reliability: String? = ""
)