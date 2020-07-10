package com.example.juniorproject.common

/**
 * 상수 정의
 */
class Constant {

    companion object {

        // retrofit
        const val SERVER_URL = "http://192.168.10.23:3000" // 서버 URL

        // realm db file
        const val REALM_DEFAULT = "DEFAULT" // realm default instance
        const val REALM_TOTAL_USER_INFO = "REALM_TOTAL_USER_INFO" // realm 전체 유저 정보 instance
        // realm function
        const val REALM_FUNC_INSERT = "REALM_FUNC_INSERT" // insert
        const val REALM_FUNC_SELECT = "REALM_FUNC_SELECT" // select

    }

}