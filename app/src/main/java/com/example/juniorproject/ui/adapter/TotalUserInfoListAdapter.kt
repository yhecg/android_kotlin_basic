package com.example.juniorproject.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.juniorproject.databinding.ItemTotalUserInfoBinding
import com.example.juniorproject.db.realm.model.RealmTotalUserInfoModel

/**
 * 메인화면에서 유저 전체 관련 정보를 보여줄 리스트뷰 어댑터
 */
class TotalUserInfoListAdapter(private val context : Context) :
    RecyclerView.Adapter<TotalUserInfoListAdapter.VHolder>() {

    class VHolder(private val binding:ItemTotalUserInfoBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(data:RealmTotalUserInfoModel){
            binding.info = data
            binding.executePendingBindings()
        }
    }

//    var list = MutableLiveData(mutableListOf<RealmTotalUserInfoModel>())
    var list = mutableListOf<RealmTotalUserInfoModel>()

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
            LayoutInflater.from(context), parent, false)
        return VHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.onBind(list[position])

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    class diffCallback:DiffUtil.ItemCallback<RealmTotalUserInfoModel>(){
        override fun areItemsTheSame(
            oldItem: RealmTotalUserInfoModel,
            newItem: RealmTotalUserInfoModel
        ): Boolean {
            return oldItem.type == newItem.type
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: RealmTotalUserInfoModel,
            newItem: RealmTotalUserInfoModel
        ): Boolean {
            return oldItem == newItem
        }

    }


}