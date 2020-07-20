package com.example.juniorproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.juniorproject.R
import com.example.juniorproject.databinding.ActivityMainBinding
import com.example.juniorproject.db.realm.RealmRepo
import com.example.juniorproject.example.mvvm_fragment.VmSharedActivity
import com.example.juniorproject.example.mvvm_activity.TestActivity
import com.example.juniorproject.ui.adapter.RealmRVAdapter
import com.example.juniorproject.ui.viewmodel.MainViewModel
import com.example.juniorproject.util.LogUtil
import io.realm.Realm
import kotlin.Exception

/**
 * 정보들을 보여주는 리스트 화면
 */
class MainActivity : BaseActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding:ActivityMainBinding // dataBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this) // 앱 구동간 한번만 해주면 된다. 메인에서 해주거나 MultiDex 에 해주면 될 듯.
        dataBindingInit()

    }

    // dataBinding 설정
    private fun dataBindingInit(){
        try {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {

                lifecycleOwner = this@MainActivity
                model = this@MainActivity.viewModel

                rvTotalUserInfoList.layoutManager = LinearLayoutManager(this@MainActivity)
                rvTotalUserInfoList.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
//                rvTotalUserInfoList.adapter = adapter

            }
            binding.rvTotalUserInfoList.adapter = adapters

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

//            viewModel.mRealm.addChangeListener(viewModel.realmListener)
//            viewModel.results = viewModel.mRealm.where(RealmTotalUserInfoModel::class.java).findAll()
//            viewModel.check.observe(this, Observer {
//                adapters.notifyDataSetChanged()
//            })

            viewModel.getDataObserved().observe(this, Observer {
                it?.let {
                    adapters.result = it
                    adapters.notifyDataSetChanged()
                }
            })

            (binding.rvTotalUserInfoList.adapter as RealmRVAdapter).result = RealmRepo.getRead(viewModel.mRealm)

        } catch (e:Exception){
            LogUtil.e(TAG, "데이터바인딩 초기화 오류 : $e")
        }
    }

    // 리스트뷰 어댑터 설정
    private var adapters = RealmRVAdapter(
        results = null, autoUpdate = true
    ).apply {
        setItemClickListener(object:RealmRVAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                try{
                    val result = RealmRepo.getRead(viewModel.mRealm)?.get(position)
                    RealmRepo.setUpdate(viewModel.mRealm, result?.idx)
                }catch (e:Exception){
                    LogUtil.e(TAG, "리스트뷰 어댑터 설정 오류 : $e")
                }
            }
        })
    }

    // 리스트뷰 어댑터 설정
//    private var adapter = TotalUserInfoListAdapter().apply {
//        setItemClickListener(object:TotalUserInfoListAdapter.ItemClickListener{
//            override fun onClick(view: View, position: Int) {
//                try{
//                    val index = binding.model?._items?.value?.get(position)?.idx
//                    RealmRepo.setUpdate(viewModel.mRealm, index!!)
//                    binding.model?.setListView()
//                }catch (e:Exception){
//                    LogUtil.e(TAG, "리스트뷰 어댑터 설정 오류 : $e")
//                }
//            }
//        })
//    }


}