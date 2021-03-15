package com.example.e_suratpermintaan.presentation.adapter

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataHistory
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ItemHistoryBinding
import com.github.vipulasri.timelineview.TimelineView

class HistoryAdapter :
    RecyclerView.Adapter<HistoryAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflter: LayoutInflater
    var dataHistorySP: ArrayList<DataHistory?> = arrayListOf()

    var onItemClick: ((List<DetailHistory?>?) -> Unit)? = null

//    private lateinit var onClickItemListener: OnClickItemListener
//
//    interface OnClickItemListener {
//        fun onClick(view: View, item: List<DetailHistory?>?)
//    }

//    fun setOnClickListener(onClickItemListener: OnClickItemListener) {
//        this.onClickItemListener = onClickItemListener
//    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::mLayoutInflter.isInitialized) {
            mLayoutInflter = LayoutInflater.from(parent.context)
        }
        val binding = ItemHistoryBinding.inflate(mLayoutInflter, parent, false)
        return TimeLineViewHolder(binding, viewType)
    }

    override fun getItemCount(): Int = dataHistorySP.size


    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val history = dataHistorySP[position]

        holder.bind(history)

//        holder.date.text = timeLineModel?.tanggal
//        holder.title.text = timeLineModel?.keterangan
//        holder.user.text = "${timeLineModel?.user} (${timeLineModel?.role})"
//        val color = timeLineModel?.warna
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            holder.timeline.marker.colorFilter =
//                BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_IN);
//        } else {
//            holder.timeline.marker.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
//        }
//
//        val ld: LayerDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_history_item) as LayerDrawable
//        val topBorder: GradientDrawable = ld.findDrawableByLayerId(R.id.left_border) as GradientDrawable
//        topBorder.setColor(Color.parseColor(color))
//
//        holder.container.background = ld
//
//        if (timeLineModel?.tombolDetail == 1) {
//            holder.detail.visibility = View.VISIBLE
//        }
//
//        holder.detail.setOnClickListener {
//            onClickItemListener.onClick(holder.itemView, timeLineModel?.detail)
//        }
    }

    inner class TimeLineViewHolder(private val binding: ItemHistoryBinding, viewType: Int) :
        RecyclerView.ViewHolder(binding.root) {
//        val title = itemView.tvTitleHistory
//        val user = itemView.tvUserHistory
//        val date = itemView.tvDateHistory
//        val timeline = itemView.timeline
//        val container = itemView.containerHistory
//        val detail = itemView.tvDetailHistory

        fun bind(history: DataHistory?) {
            binding.tvTitleHistory.text = history?.keterangan
            binding.tvUserHistory.text = "${history?.user} (${history?.role})"
            binding.tvDateHistory.text = history?.tanggal
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.timeline.marker.colorFilter =
                    BlendModeColorFilter(Color.parseColor(history?.warna), BlendMode.SRC_IN)
            } else {
                binding.timeline.marker.setColorFilter(
                    Color.parseColor(history?.warna),
                    PorterDuff.Mode.SRC_IN
                )
            }

            val ld: LayerDrawable = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.border_history_item
            ) as LayerDrawable
            val topBorder: GradientDrawable =
                ld.findDrawableByLayerId(R.id.left_border) as GradientDrawable
            topBorder.setColor(Color.parseColor(history?.warna))

            binding.containerHistory.background = ld

            if (history?.tombolDetail == 1) {
                binding.tvDetailHistory.visibility = View.VISIBLE
            } else {
                binding.tvDetailHistory.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(history?.detail)
            }
        }

        init {
            binding.timeline.initLine(viewType)
        }
    }
}