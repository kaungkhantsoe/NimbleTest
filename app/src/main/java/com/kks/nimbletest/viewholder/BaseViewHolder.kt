package com.kks.nimbletest.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<O>(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    abstract fun onBindView(data: O)
}

