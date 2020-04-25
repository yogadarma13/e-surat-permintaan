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
            ITEM_A -> ViewHolderItemB(inflater.inflate(R.layout.notifikasi_read_list, null))
            else -> ViewHolderItemA(inflater.inflate(R.layout.notifikasi_unread_list, null))
        }
    }

    override fun getItemCount(): Int = notifList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (viewType[position]) {
            ITEM_A -> {
                val viewHolderA = holder as ViewHolderItemA
                viewHolderA.date.text = "READ"
            }
            ITEM_B -> {
                val viewHolderB = holder as ViewHolderItemB
                viewHolderB.date.text = "UNREAD"
            }
        }
    }

    override fun getItemViewType(position: Int) : Int {
        return viewType[position]
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tvDateNotif)
        val note: TextView = itemView.findViewById(R.id.tvKeteranganNotif)
        val spCode: TextView = itemView.findViewById(R.id.tvSuratPermintaanNotif)
        val user: TextView = itemView.findViewById(R.id.tvUserNotif)
        val role: TextView = itemView.findViewById(R.id.tvRoleNotif)
    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.tvDateNotif)
        val note: TextView = itemView.findViewById(R.id.tvKeteranganNotif)
        val spCode: TextView = itemView.findViewById(R.id.tvSuratPermintaanNotif)
        val user: TextView = itemView.findViewById(R.id.tvUserNotif)
        val role: TextView = itemView.findViewById(R.id.tvRoleNotif)

    }
}