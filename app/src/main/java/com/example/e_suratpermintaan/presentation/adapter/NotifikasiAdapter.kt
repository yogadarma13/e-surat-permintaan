package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_suratpermintaan.R

class NotifikasiAdapter(private val notifList: ArrayList<Any>, private val viewType: ArrayList<Int>):
    RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>(){

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
        val notif = notifList[position]
        when (notif) {
            ITEM_A -> {
                val viewHolderA = holder as ViewHolderUnread
                viewHolderA.dateUnread.text = "Tess A"
            }
            else -> {
                val viewHolderB = holder as ViewHolderRead
                viewHolderB.date.text = "Tess B"
            }
        }
    }

    override fun getItemViewType(position: Int) : Int = viewType[position]

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderUnread(itemView: View) : ViewHolder(itemView) {
        val dateUnread: TextView = itemView.findViewById(R.id.tvDateNotifUnread)
        val noteUnread: TextView = itemView.findViewById(R.id.tvKeteranganNotifUnread)
        val spCodeUnread: TextView = itemView.findViewById(R.id.tvSuratPermintaanNotifUnread)
        val userUnread: TextView = itemView.findViewById(R.id.tvUserNotifUnread)
        val roleUnread: TextView = itemView.findViewById(R.id.tvRoleNotifUnread)

    }

    inner class ViewHolderRead(itemView: View) : ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.tvDateNotif)
        val note: TextView = itemView.findViewById(R.id.tvKeteranganNotif)
        val spCode: TextView = itemView.findViewById(R.id.tvSuratPermintaanNotif)
        val user: TextView = itemView.findViewById(R.id.tvUserNotif)
        val role: TextView = itemView.findViewById(R.id.tvRoleNotif)

    }


}