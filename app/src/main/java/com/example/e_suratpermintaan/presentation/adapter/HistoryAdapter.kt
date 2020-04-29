package com.example.e_suratpermintaan.presentation.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataHistory
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.example.e_suratpermintaan.R
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter():
    RecyclerView.Adapter<HistoryAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflter: LayoutInflater
    var dataHistorySP: ArrayList<DataHistory?> = arrayListOf()

    private lateinit var onClickItemListener: OnClickItemListener

    interface OnClickItemListener {
        fun onClick(view: View, item: List<DetailHistory?>?)
    }

    fun setOnClickListener(onClickItemListener: OnClickItemListener) {
        this.onClickItemListener = onClickItemListener
    }


    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::mLayoutInflter.isInitialized) {
            mLayoutInflter = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(mLayoutInflter.inflate(R.layout.item_history, parent, false), viewType)
    }

    override fun getItemCount(): Int {
        return dataHistorySP!!.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val timeLineModel = dataHistorySP?.get(position)

        holder.date.text = timeLineModel?.tanggal
        holder.title.text = timeLineModel?.keterangan
        holder.user.text = "${timeLineModel?.user} (${timeLineModel?.role})"
        var color = timeLineModel?.warna

        holder.timeline.marker.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)

        val ld: LayerDrawable = holder.itemView.context.resources.getDrawable(R.drawable.border_history_item) as LayerDrawable
        val topBorder: GradientDrawable = ld.findDrawableByLayerId(R.id.left_border) as GradientDrawable
        topBorder.setColor(Color.parseColor(color))

        holder.container.background = ld

        if (timeLineModel?.tombolDetail == 1) {
            holder.detail.visibility = View.VISIBLE
        }

        holder.detail.setOnClickListener {
            onClickItemListener.onClick(holder.itemView, timeLineModel?.detail)
        }
    }

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.tvTitleHistory
        val user = itemView.tvUserHistory
        val date = itemView.tvDateHistory
        val timeline = itemView.timeline
        val container = itemView.containerHistory
        val detail = itemView.tvDetailHistory

        init {
            timeline.initLine(viewType)
        }
    }
}