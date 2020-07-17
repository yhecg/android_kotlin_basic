package com.example.juniorproject.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.juniorproject.databinding.ItemTotalUserInfoBinding
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * 메인화면에서 유저 전체 관련 정보를 보여줄 리스트뷰 어댑터
 */
class ListAdapter(results:RealmResults<RealmTotalUserInfoModel>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<RealmTotalUserInfoModel, ListAdapter.VHolder>(results, autoUpdate) {

    class VHolder(private val binding:ItemTotalUserInfoBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(data:RealmTotalUserInfoModel){
            binding.info = data
            binding.executePendingBindings()
        }
    }

    var items = ArrayList<RealmTotalUserInfoModel>()

    var item = results

    // 클릭 인터페이스 정의
    interface ItemClickListener{
        fun onClick(view:View, position:Int)
    }

    // 클릭 리스너 선언
    private lateinit var itemClickListener:ItemClickListener

    // 클릭 리스너 등록 메소드
    fun setItemClickListener(itemClickListener:ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val binding = ItemTotalUserInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return VHolder(binding)
    }

//    override fun getItemCount(): Int = items.size
    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.onBind(items[position])
        holder.onBind(item[position]!!)

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }


}