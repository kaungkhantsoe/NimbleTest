package com.kks.nimbletest.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kks.nimbletest.R
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.util.listeners.RecyclerViewItemClickListener

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

class SurveyViewHolder(
    view: View,
    private val recyclerViewItemClickListener: RecyclerViewItemClickListener,
    private val requestManager: RequestManager,
) : BaseViewHolder<SurveyResponse>(view) {
    lateinit var image: ImageView

    override fun onBindView(data: SurveyResponse) {
        image = itemView.findViewById(R.id.iv_survey)
        requestManager
            .load(data.attributes?.cover_image_url)
            .into(image)

        itemView.setOnClickListener {
            recyclerViewItemClickListener.onItemClick(adapterPosition)
        }
    }
}