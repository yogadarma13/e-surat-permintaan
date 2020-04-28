package com.example.e_suratpermintaan.presentation.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ReadItem
import com.e_suratpermintaan.core.domain.entities.responses.UnreadItem
import com.example.e_suratpermintaan.R

class NotifikasiAdapter(): RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>(){

    val notifList: ArrayList<Any> = arrayListOf()
    val viewType: ArrayList<Int> = arrayListOf()

    private lateinit var onClickItemListener: OnClickItemListener

    interface OnClickItemListener{
        fun onClick(view: View, item: Any)
    }

    fun setOnClickListener(onClickItemListener: OnClickItemListener){
        this.onClickItemListener = onClickItemListener
    }

    companion object {
        val ITEM_A = 0
        val ITEM_B = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            ITEM_A -> ViewHolderUnread(inflater.inflate(R.layout.notifikasi_unread_list, null))
            else -> ViewHolderRead(inflater.inflate(R.layout.notifikasi_read_list, null))
        }
    }

    override fun getItemCount(): Int = notifList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (viewType[position]) {
            ITEM_A -> {
                val data = notifList.get(position) as UnreadItem
                val viewHolderA = holder as ViewHolderUnread
                viewHolderA.dateUnread.text = data.tanggal
                viewHolderA.noteUnread.text = data.keterangan
                viewHolderA.roleUnread.text = data.role
                viewHolderA.spCodeUnread.text = data.kode_sp
                viewHolderA.userUnread.text = data.user

                if ((data.warna.toString().isNotEmpty())) {
                    viewHolderA.roleUnread.background.setColorFilter(
                        Color.parseColor(data.warna),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
            ITEM_B -> {
                val data = notifList.get(position) as ReadItem
                val viewHolderB = holder as ViewHolderRead
                viewHolderB.date.text = data.tanggal
                viewHolderB.note.text = data.keterangan
                viewHolderB.role.text = data.role
                viewHolderB.spCode.text = data.kode_sp
                viewHolderB.user.text = data.user

                if ((data.warna.toString().isNotEmpty())) {
                    viewHolderB.role.background.setColorFilter(
                        Color.parseColor(data.warna),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }

        val dataNotifClick = notifList[holder.layoutPosition]
        holder.itemView.setOnClickListener {
            onClickItemListener.onClick(holder.itemView, dataNotifClick)
        }
    }

    override fun getItemViewType(position: Int) : Int = viewType[position]

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderUnread(itemView: View) : ViewHolder(itemView) {
        val dateUnread: TextView = itemView.findViewById(R.id.tvDateNotifUnread)
        val noteUnread: TextView = itemView.findViewById(R.id.tvKeteranganNotifUnread)
        val spCodeUnread: TextView = itemView.findViewById(R.id.tvKodeSpNotifUnread)
        val userUnread: TextView = itemView.findViewById(R.id.tvUserNotifUnread)
        val roleUnread: TextView = itemView.findViewById(R.id.tvRoleNotifUnread)

    }

    inner class ViewHolderRead(itemView: View) : ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.tvDateNotifRead)
        val note: TextView = itemView.findViewById(R.id.tvKeteranganNotifRead)
        val spCode: TextView = itemView.findViewById(R.id.tvKodeSpNotifRead)
        val user: TextView = itemView.findViewById(R.id.tvUserNotifRead)
        val role: TextView = itemView.findViewById(R.id.tvRoleNotifRead)
    }
}