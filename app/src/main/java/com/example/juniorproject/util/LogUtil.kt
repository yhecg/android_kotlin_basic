package com.example.juniorproject.util

import android.util.Log

/**
 * 로그를 보다 편하게 보여주기 위한 로그 유틸
 */
class LogUtil {

    companion object{

        fun buildLogMsg(message:String): String {
            var ste:StackTraceElement = Thread.currentThread().stackTrace[4]
            var sb:StringBuilder = StringBuilder()
            sb.append("[")
            sb.append(ste.getFileName().replace(".java", ""))
            sb.append("::")
            sb.append(ste.getMethodName())
            sb.append("]")
            sb.append(message)
            return sb.toString()
        }

        fun d(tag:String, message:String){
            if(message.length > 3000){
                Log.d(tag, message.substring(0, 3000))
                d(tag, message.substring(3000))
            }else{
                Log.d(tag, buildLogMsg(message))
            }
        }

        fun e(tag:String, message:String){
            Log.e(tag, buildLogMsg(message))
        }

        fun w(tag:String, message:String){
            Log.w(tag, buildLogMsg(message))
        }

        fun i(tag:String, message:String){
            Log.i(tag, buildLogMsg(message))
        }

        fun v(tag:String, message:String){
            Log.v(tag, buildLogMsg(message))
        }

    }

}