package com.example.juniorproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.juniorproject.R
import com.example.juniorproject.databinding.ActivityMainBinding
import com.example.juniorproject.db.realm.RealmManager
import com.example.juniorproject.db.realm.module.RealmTotalUserInfoModule
import com.example.juniorproject.network.RetrofitApiId
import com.example.juniorproject.network.RetrofitClient
import com.example.juniorproject.network.dto.ResponseCommonData
import com.example.juniorproject.ui.adapter.TotalUserInfoListAdapter
import com.example.juniorproject.ui.dto.TotalUserInfoDTO
import com.example.juniorproject.util.LogUtil
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Exception

/**
 * 정보들을 보여주는 리스트 화면
 */
class MainActivity : BaseActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding:ActivityMainBinding // dataBinding

    private lateinit var mRealm:Realm // realm

    private lateinit var adapter:TotalUserInfoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBindingInit()
        realmInit()
        rvInit()

    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close() // realm 닫기 - 메모리 Leak 방지
    }

    // dataBinding 설정
    private fun dataBindingInit(){
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            binding.activity = this@MainActivity
        }catch (e:Exception){
            LogUtil.e(TAG, "데이터바인딩 초기화 오류 : $e")
        }
    }

    // 리스트뷰 설정
    private fun rvInit() {
        try {
            adapter = TotalUserInfoListAdapter(this)
            binding.rvTotalUserInfoList.layoutManager = LinearLayoutManager(this)
            binding.rvTotalUserInfoList.adapter = adapter

            adapter.setItemClickListener(object:TotalUserInfoListAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    setRealmDataUpdate(position)
                }
            })

            setListView()
        } catch (e: Exception) {
            LogUtil.e(TAG, "리스트뷰 데이터 처리 오류 : $e")
        }
    }

    // realm 설정
    private fun realmInit(){
        try {
            Realm.init(this) // 앱 구동간 한번만 해주면 된다. 메인에서 해주거나 MultiDex 에 해주면 될 듯.
            mRealm = RealmManager.getInstance()!!
        }catch (e:Exception){
            LogUtil.e(TAG, "realm 초기화 오류 : $e")
        }
    }

    // 저장된 데이터 불러와서 리스트뷰 연결
    private fun setListView() {
        try {
            val dbResult = getRealmData()
            val dbResultSize:Int = dbResult!!.size
            adapter.list.clear()
            if(dbResultSize != 0) {
                for (i in 0 until dbResultSize) {
                    val dto = TotalUserInfoDTO(dbResult[i].idx,
                        (dbResult[i].type), dbResult[i].error, dbResult[i].estimated_data,
                        dbResult[i].original_data, dbResult[i].reliability
                    )
                    adapter.list.add(dto)
                }
                adapter.notifyDataSetChanged()
                binding.rvTotalUserInfoList.scrollToPosition(dbResultSize - 1)
            }else{
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            LogUtil.e(TAG, "리스트뷰 데이터 처리 오류 : $e")
        }
    }

    // realm 데이터 수정
    fun setRealmDataUpdate(position:Int){
        try {
            mRealm.beginTransaction()
            val result: RealmTotalUserInfoModule? =
                mRealm.where(RealmTotalUserInfoModule::class.java).
                equalTo("idx", adapter.list[position].idx).findFirst()
            result?.type = "수정완료"
            mRealm.commitTransaction()

            setListView()
        }catch (e:Exception){
            LogUtil.e(TAG, "realm 데이터 수정 오류 : $e")
        }
    }

    // realm 데이터 삭제
    fun setRealmDataDelete(){
        try {
            mRealm.beginTransaction()
            val result: RealmResults<RealmTotalUserInfoModule> =
                mRealm.where(RealmTotalUserInfoModule::class.java).findAll()
            result.deleteAllFromRealm()
            mRealm.commitTransaction()

            setListView()
        }catch (e:Exception){
            LogUtil.e(TAG, "realm 데이터 삭제 오류 : $e")
        }
    }

    // realm 데이터 보기
    fun getRealmData(): MutableList<RealmTotalUserInfoModule>? {
        try{
            mRealm.beginTransaction()
            val result:RealmResults<RealmTotalUserInfoModule> =
                mRealm.where(RealmTotalUserInfoModule::class.java).findAll()

            // RealmResults 객체 -> 리스트로 변환
            val dbList: MutableList<RealmTotalUserInfoModule>? = mRealm.copyFromRealm(result)

            // 리스트 -> json 으로 변환
            val gson = GsonBuilder().create()
            val dbJson = gson.toJson(dbList)
            LogUtil.d(TAG, "realm 데이터 json : $dbJson")

            mRealm.commitTransaction()

            return dbList
        }catch (e:Exception){
            LogUtil.e(TAG, "realm data 가져오기 오류 : $e")
            return null
        }
    }

    // 서버에서 받은 데이터를 저장
    private fun setRealmDataSave(infoList:ArrayList<ResponseCommonData>, count:Int){
        try{
            if(count < infoList.size){
                val info: ResponseCommonData = infoList[count]
                val gson = GsonBuilder().create()
                val infoStr = gson.toJson(info)
                val infoJson:JSONObject = JSONObject(infoStr)
                val keys:Iterator<String> = infoJson.keys()

                // DB realm 작업 시작
                mRealm.beginTransaction()
                val idx = mRealm.where(RealmTotalUserInfoModule::class.java).max("idx")
                val nextId = if(idx == null){
                    1
                }else{
                    idx.toInt()+1
                }
                val module: RealmTotalUserInfoModule = mRealm.createObject(
                    RealmTotalUserInfoModule::class.java, nextId)

                while(keys.hasNext()){
                    val key:String = keys.next()

                    val sb:StringBuilder = StringBuilder()
                    val jsonSize:Int = infoJson.getJSONArray(key).length()
                    for(i in 0 until jsonSize){
                        if(i != 0){
                            sb.append(",")
                        }
                        sb.append(infoJson.getJSONArray(key)[i])
                    }
                    LogUtil.d(TAG, " [ key : $key ] - [ value : $sb ]")
                    when(key){
                        "type" -> module.type = sb.toString()
                        "error" -> module.error = sb.toString()
                        "estimated_data" -> module.estimated_data = sb.toString()
                        "original_data" -> module.original_data = sb.toString()
                        "reliability" -> module.reliability = sb.toString()
                    }
                }
                // DB realm 작업 종료
                mRealm.commitTransaction()

                setRealmDataSave(infoList, count+1)
            }else{
                // 저장이 다 되었으면 데이터를 리스트뷰에 뿌려준다
                setListView()
            }
        }catch (e:Exception){
            LogUtil.e(TAG, "서버에서 받은 데이터 변환 오류 : $e")
        }
    }

    // 서버에서 데이터 받기
    fun getServerData(){
        try{
            RetrofitClient.getInstance()?.requestTotalUserInfo(mHandler)
        }catch (e:Exception){
            LogUtil.e(TAG, "api 통신 오류 : $e")
        }
    }

    // 서버에서 받은 데이터 Handler
    private var mHandler = @SuppressLint("HandlerLeak")
    object:Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == RetrofitApiId.RESPONSE_TOTAL_USER_INFO){
                try {
                    setRealmDataSave(msg.obj as ArrayList<ResponseCommonData>, 0)
                }catch (e:Exception){
                    LogUtil.e(TAG, "통신 데이터 Response 핸들러 오류 : $e")
                }
            }
        }
    }


}