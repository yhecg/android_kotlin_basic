package com.example.juniorproject.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesUtil {

    private val TAG = AesUtil::class.java.simpleName

    private lateinit var mContext:Context

    constructor(){}

    constructor(context: Context){
        this.mContext = context
    }

    // Android-Security : EncryptedSharedPreferences
    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptedSharedPreferences(){

        // 마스터키 - 대칭키 생성
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences = EncryptedSharedPreferences.create(
            "sharedPreferenceDbFileName",
            masterKeyAlias,
            mContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // 시스템 무결성 검증기 System Integrity Verifier
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val spfEditor: SharedPreferences.Editor = sharedPreferences.edit()
        spfEditor.putString("name", "이름").apply()

        val name = sharedPreferences.getString("name", "값이 없을시 기본값")
        name?.let { Log.d(TAG, it) }
    }




    // EncryptedFile 파일 읽기
    @RequiresApi(Build.VERSION_CODES.M)
    fun readFile(){
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val fileToRead = "enc_file_test.txt"
        val encryptedFile = EncryptedFile.Builder(
            File(getSaveFolder(), fileToRead),
            mContext,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileInput().use { fileInputStream ->
            val byteStream = ByteArrayOutputStream()
            var nextByte = fileInputStream.read()
            while(nextByte != -1){
                byteStream.write(nextByte)
                nextByte = fileInputStream.read()
            }
            val fileContents = byteStream.toByteArray()
            val result = String(fileContents, charset("UTF-8"))
            LogUtil.d(TAG, "파일 복호화 : $result")
            fileInputStream.close()
        }
    }

    // EncryptedFile 파일 쓰기
    @RequiresApi(Build.VERSION_CODES.M)
    fun writeFile(){
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val fileToWrite = "enc_file_test.txt"
        val file = File(getSaveFolder(), fileToWrite)
        if(file.exists()){
            file.delete()
        }
        val encryptedFile = EncryptedFile.Builder(
            File(getSaveFolder(), fileToWrite),
            mContext,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().use { fileOutputStream ->
            fileOutputStream.write("동해물과 백두산".toByteArray())
        }

//        val outputStream:FileOutputStream? = encryptedFile.openFileOutput()
//        outputStream?.apply {
//            write("Tsettest".toByteArray(Charset.forName("UTF-8")))
//            flush()
//            close()
//        }

    }

    // 폴더 경로
    private fun getSaveFolder(): String {
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/test" )
        if(!dir.exists()){
            LogUtil.d(TAG, "폴더 없음")
            dir.mkdirs()
//            dir.mkdir()
        }else{
            LogUtil.d(TAG, "폴더 있음")
        }
        return dir.absolutePath
    }




    // 암-복호화
    fun encDec() {

        val KEY:String = "63e2c0aa89c5cb1055c0ce867ec358ac"
        val KEY_256 = KEY.substring(0, 256/8) // 64
        val KEY_128 = KEY.substring(0, 128/8) // 32
        val keySpec = SecretKeySpec(KEY_256.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(KEY_128.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

        // 암호화
        val plainText = "동해물과 백두산이 마르고 닳도록 하나님이 보우하사 우리나라만세"
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        val encodeByte = Base64.encode(encrypted, Base64.DEFAULT)

        // 복호화
        val decodeByte:ByteArray = Base64.decode(encodeByte, Base64.DEFAULT)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decrypted = cipher.doFinal(decodeByte)
        val result = String(decrypted, charset("UTF-8"))
        LogUtil.d(TAG, "[ 평문 : $plainText ] [ 복호화한 암호문 : $result ]")
        if(plainText == result){
            LogUtil.d(TAG, "평문과 복호화값 같음")
        }else{
            LogUtil.d(TAG, "평문과 복호화값 다름")
        }

    }

}