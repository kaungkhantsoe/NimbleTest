package com.kks.nimbletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.kks.nimbletest.R
import com.kks.nimbletest.viewholder.SurveyViewHolder
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.util.listeners.RecyclerViewItemClickListener

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

class SurveyAdapter(
    private val recyclerViewItemClickListener: RecyclerViewItemClickListener,
    private val requestManager: RequestManager
) : BaseRecyclerAdapter<SurveyViewHolder, SurveyResponse>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder =
        SurveyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_survey, parent, false),
            recyclerViewItemClickListener,
            requestManager
        )
}