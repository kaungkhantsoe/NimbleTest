package com.kks.nimbletest.adapter

import androidx.recyclerview.widget.RecyclerView
import com.kks.nimbletest.viewholder.BaseViewHolder


abstract class BaseRecyclerAdapter<T : BaseViewHolder<O>, O> :
    RecyclerView.Adapter<T>() {
    private var mData: ArrayList<O> = ArrayList()

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.onBindView(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun addAll(newItems: List<O>) {
        mData.addAll(newItems)
        notifyItemRangeInserted(mData.size - newItems.size, newItems.size)
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun getNewData() = mData
}