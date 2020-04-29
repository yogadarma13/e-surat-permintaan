package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.PersyaratanItem
import com.example.e_suratpermintaan.R

class DetailHistoryAdapter(): RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {

    val historyList: ArrayList<ItemsDetailHistory> = arrayListOf()
    val viewType: ArrayList<String> = arrayListOf()
    val persyaratanList = mutableMapOf<String, String>()

//    private lateinit var onClickItemListener: OnClickItemListener
//
//    interface OnClickItemListener{
//        fun onClick(view: View, item: Any)
//    }
//
//    fun setOnClickListener(onClickItemListener: OnClickItemListener){
//        this.onClickItemListener = onClickItemListener
//    }

    companion object {
        val SPA = 0
        val SPB = 1
        val SPS = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            SPA -> ViewHolderSPA(inflater.inflate(R.layout.detail_history_spa_item, parent, false))
            SPB -> ViewHolderSPB(inflater.inflate(R.layout.detail_history_spb_item, parent, false))
            else -> ViewHolderSPS(inflater.inflate(R.layout.detail_history_sps_item, parent, false))
        }
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historyList.get(position)
        when(getItemViewType(position)){
            SPA -> {
                val viewHolderSPA = holder as ViewHolderSPA

                val expanded = data.expanded
                if (expanded) {
                    viewHolderSPA.expandedSPA.visibility = View.VISIBLE
                    viewHolderSPA.jenisDetailSPA.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                } else {
                    viewHolderSPA.expandedSPA.visibility = View.GONE
                    viewHolderSPA.jenisDetailSPA.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                }

                viewHolderSPA.jenisDetailSPA.text = data.idBarang
                viewHolderSPA.kodeDetailSPA.text = data.kodePekerjaan
                viewHolderSPA.satuanDetailSPA.text = data.idSatuan
                viewHolderSPA.kapasitasDetailSPA.text = data.kapasitas
                viewHolderSPA.waktuDetailSPA.text = data.waktuPemakaian
                viewHolderSPA.merkDetailSPA.text = data.merk
                viewHolderSPA.qtyDetailSPA.text = data.qty
                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPA.keteranganDetailSPA.text = dataKeterangan

                viewHolderSPA.jenisDetailSPA.setOnClickListener {
                    var expanded  = data.expanded
                    data.expanded = !expanded
                    notifyItemChanged(position)
                }

            }

            SPB -> {
                val viewHolderSPB = holder as ViewHolderSPB

                val expanded = data.expanded
                if (expanded) {
                    viewHolderSPB.expandedSPB.visibility = View.VISIBLE
                    viewHolderSPB.jenisDetailSPB.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                } else {
                    viewHolderSPB.expandedSPB.visibility = View.GONE
                    viewHolderSPB.jenisDetailSPB.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                }

                viewHolderSPB.jenisDetailSPB.text = data.idBarang
                viewHolderSPB.kodeDetailSPB.text = data.kodePekerjaan
                viewHolderSPB.satuanDetailSPB.text = data.idSatuan
                viewHolderSPB.fungsiDetailSPB.text = data.fungsi
                viewHolderSPB.targetDetailSPB.text = data.target
                viewHolderSPB.qtyDetailSPB.text = data.qty
                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPB.keteranganDetailSPB.text = dataKeterangan

                viewHolderSPB.jenisDetailSPB.setOnClickListener {
                    var expanded  = data.expanded
                    data.expanded = !expanded
                    notifyItemChanged(position)
                }
            }

            SPS -> {
                val viewHolderSPS = holder as ViewHolderSPS

                val expanded = data.expanded
                if (expanded) {
                    viewHolderSPS.expandedSPS.visibility = View.VISIBLE
                    viewHolderSPS.jenisDetailSPS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                } else {
                    viewHolderSPS.expandedSPS.visibility = View.GONE
                    viewHolderSPS.jenisDetailSPS.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                }

                viewHolderSPS.jenisDetailSPS.text = data.idBarang
                viewHolderSPS.kodeDetailSPS.text = data.kodePekerjaan
                viewHolderSPS.satuanDetailSPS.text = data.idSatuan
                viewHolderSPS.waktuDetailSPS.text = data.waktuPelaksanaan
                viewHolderSPS.qtyDetailSPS.text = data.qty
                var syarat: String? = ""

                data.persyaratan?.forEach {
                    val dataSyarat = it as PersyaratanItem
                    syarat += "${persyaratanList[dataSyarat.persyaratan]}\n"
                }
                viewHolderSPS.persyaratanDetailSPS.text = syarat


                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPS.keteranganDetailSPS.text = dataKeterangan

                viewHolderSPS.jenisDetailSPS.setOnClickListener {
                    var expanded  = data.expanded
                    data.expanded = !expanded
                    notifyItemChanged(position)
                }

            }
        }
    }

    override fun getItemViewType(position: Int) : Int {
        if (viewType[position].equals("SPA")){
            return SPA
        }
        else if (viewType[position].equals("SPB")) {
            return SPB
        } else {
            return SPS
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderSPA(itemView: View) : ViewHolder(itemView) {
        val jenisDetailSPA: TextView = itemView.findViewById(R.id.tvJenisDetailSPA)
        val kodeDetailSPA: TextView = itemView.findViewById(R.id.tvKodeDetailSPA)
        val satuanDetailSPA: TextView = itemView.findViewById(R.id.tvSatuanDetailSPA)
        val kapasitasDetailSPA: TextView = itemView.findViewById(R.id.tvKapasitasDetailSPA)
        val merkDetailSPA: TextView = itemView.findViewById(R.id.tvMerkDetailSPA)
        val waktuDetailSPA: TextView = itemView.findViewById(R.id.tvWaktuDetailSPA)
        val qtyDetailSPA: TextView = itemView.findViewById(R.id.tvQtySPA)
        val keteranganDetailSPA: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPA)
        val expandedSPA: ConstraintLayout = itemView.findViewById(R.id.constarintExpandSPA)
    }

    inner class ViewHolderSPB(itemView: View) : ViewHolder(itemView){
        val jenisDetailSPB: TextView = itemView.findViewById(R.id.tvJenisDetailSPB)
        val kodeDetailSPB: TextView = itemView.findViewById(R.id.tvKodeDetailSPB)
        val satuanDetailSPB: TextView = itemView.findViewById(R.id.tvSatuanDetailSPB)
        val fungsiDetailSPB: TextView = itemView.findViewById(R.id.tvFungsiDetailSPB)
        val targetDetailSPB: TextView = itemView.findViewById(R.id.tvTargetDetailSPB)
        val qtyDetailSPB: TextView = itemView.findViewById(R.id.tvQtySPB)
        val keteranganDetailSPB: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPB)
        val expandedSPB: ConstraintLayout = itemView.findViewById(R.id.constarintExpandSPB)

    }

    inner class ViewHolderSPS(itemView: View) : ViewHolder(itemView){
        val jenisDetailSPS: TextView = itemView.findViewById(R.id.tvJenisDetailSPS)
        val kodeDetailSPS: TextView = itemView.findViewById(R.id.tvKodeDetailSPS)
        val satuanDetailSPS: TextView = itemView.findViewById(R.id.tvSatuanDetailSPS)
        val persyaratanDetailSPS: TextView = itemView.findViewById(R.id.tvPersyaratanDetailSPS)
        val waktuDetailSPS: TextView = itemView.findViewById(R.id.tvWaktuDetailSPS)
        val qtyDetailSPS: TextView = itemView.findViewById(R.id.tvQtySPS)
        val keteranganDetailSPS: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPS)
        val expandedSPS: ConstraintLayout = itemView.findViewById(R.id.constarintExpandSPS)
    }
}