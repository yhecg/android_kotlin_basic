package com.example.juniorproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.juniorproject.R
import com.example.juniorproject.databinding.ActivityMainBinding
import com.example.juniorproject.db.realm.RealmManager
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.example.mvvm_fragment.VmSharedActivity
import com.example.juniorproject.example.mvvm_activity.TestActivity
import com.example.juniorproject.network.RetrofitApiId
import com.example.juniorproject.network.RetrofitClient
import com.example.juniorproject.network.dto.ResponseCommonData
import com.example.juniorproject.ui.adapter.TotalUserInfoListAdapter
import com.example.juniorproject.ui.viewmodel.MainViewModel
import com.example.juniorproject.util.LogUtil
import io.realm.Realm
import io.realm.RealmResults
import kotlin.Exception

/**
 * 정보들을 보여주는 리스트 화면
 */
class MainActivity : BaseActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding:ActivityMainBinding // dataBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var mRealm:Realm // realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realmInit()
        dataBindingInit()
        setListView()

    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close() // realm 닫기 - 메모리 Leak 방지
    }

    // 리스트뷰 어댑터 설정
    private var adapter = TotalUserInfoListAdapter(this).apply {
        setItemClickListener(object:TotalUserInfoListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                try{
                    setRealmDataUpdate(position)
                }catch (e:Exception){
                    LogUtil.e(TAG, "리스트뷰 어댑터 설정 오류 : $e")
                }
            }
        })
    }

    // dataBinding 설정
    private fun dataBindingInit(){
        try {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
                lifecycleOwner = this@MainActivity
                vm = this@MainActivity.viewModel
                act = this@MainActivity

                rvTotalUserInfoList.layoutManager = LinearLayoutManager(this@MainActivity)
                rvTotalUserInfoList.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
                rvTotalUserInfoList.adapter = adapter

            }
            viewModel.showAct.observe(this, Observer {
                when (it) {
                    1 -> {
                        val intent = Intent(this, TestActivity::class.java)
                        startActivity(intent)
                    }2 -> {
                        val intent = Intent(this, VmSharedActivity::class.java)
                        startActivity(intent)
                    }
                }
            })

        } catch (e:Exception){
            LogUtil.e(TAG, "데이터바인딩 초기화 오류 : $e")
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
            adapter.list.clear()
            adapter.list.addAll(dbResult!!)
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            LogUtil.e(TAG, "리스트뷰 데이터 처리 오류 : $e")
        }
    }

    // realm 데이터 수정
    fun setRealmDataUpdate(position:Int){
        try {
            mRealm.beginTransaction()
            val result: RealmTotalUserInfoModel? =
                mRealm.where(RealmTotalUserInfoModel::class.java).
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
            val result: RealmResults<RealmTotalUserInfoModel> =
                mRealm.where(RealmTotalUserInfoModel::class.java).findAll()
            result.deleteAllFromRealm()
            mRealm.commitTransaction()

            setListView()
        }catch (e:Exception){
            LogUtil.e(TAG, "realm 데이터 삭제 오류 : $e")
        }
    }

    // realm 데이터 보기
    fun getRealmData(): MutableList<RealmTotalUserInfoModel>? {
        try{
            mRealm.beginTransaction()
            val result:RealmResults<RealmTotalUserInfoModel> =
                mRealm.where(RealmTotalUserInfoModel::class.java).findAll()
            val dbList: MutableList<RealmTotalUserInfoModel>? = mRealm.copyFromRealm(result)
            mRealm.commitTransaction()
            LogUtil.d(TAG, "realm 데이터 json : " + result.asJSON())
            return dbList
        }catch (e:Exception){
            LogUtil.e(TAG, "realm data 가져오기 오류 : $e")
            return null
        }
    }

    // Realm PrimaryKey Index 구하기
    private fun getPrimaryKeyIndex():Int{
        mRealm.beginTransaction()
        val idx = mRealm.where(RealmTotalUserInfoModel::class.java).max("idx")
        val nextId = if(idx == null){
            1
        }else{
            idx.toInt()+1
        }
        mRealm.commitTransaction()
        return nextId
    }

    // 서버에서 받은 데이터를 저장
    private fun setRealmDataSave(infoList:MutableList<ResponseCommonData>){
        try{
            for(i in 0 until infoList.size){
                val count = getPrimaryKeyIndex()
                mRealm.beginTransaction()
                val model: RealmTotalUserInfoModel = mRealm.createObject(
                    RealmTotalUserInfoModel::class.java, count)
                model.type = infoList[i].type
                model.error = infoList[i].error.toString()
                model.estimated_data = infoList[i].estimated_data.toString()
                model.original_data = infoList[i].original_data.toString()
                model.reliability = infoList[i].reliability.toString()
                mRealm.commitTransaction()
            }
            setListView()
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
                    setRealmDataSave(msg.obj as MutableList<ResponseCommonData>)
//                    setRealmDataSave(msg.obj as ArrayList<ResponseCommonData>, 0)
                }catch (e:Exception){
                    LogUtil.e(TAG, "통신 데이터 Response 핸들러 오류 : $e")
                }
            }
        }
    }


}