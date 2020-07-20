package com.example.juniorproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.juniorproject.databinding.ItemTotalUserInfoBinding
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel
import com.example.juniorproject.ui.MainActivity
import com.example.juniorproject.util.LogUtil
import io.realm.*


class RealmRVAdapter(results: RealmResults<RealmTotalUserInfoModel>?, autoUpdate:Boolean)
    : RealmRecyclerViewAdapter<RealmTotalUserInfoModel, RealmRVAdapter.VHolder>(results, autoUpdate) {

    private val TAG = RealmRVAdapter::class.java.simpleName

    class VHolder(private val binding: ItemTotalUserInfoBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: RealmTotalUserInfoModel){
            binding.info = data
            binding.executePendingBindings()
        }
    }

//    var result = results
    var result:RealmResults<RealmTotalUserInfoModel>? = null

    override fun getItemCount(): Int = result?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val binding = ItemTotalUserInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return VHolder(binding)
    }

    // 클릭 인터페이스 정의
    interface ItemClickListener{
        fun onClick(view: View, position:Int)
    }

    // 클릭 리스너 선언
    private lateinit var itemClickListener:ItemClickListener

    // 클릭 리스너 등록 메소드
    fun setItemClickListener(itemClickListener:ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.onBind(result?.get(position)!!)

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

}