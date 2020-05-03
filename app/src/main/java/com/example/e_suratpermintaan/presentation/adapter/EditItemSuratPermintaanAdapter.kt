package com.example.e_suratpermintaan.presentation.adapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.e_suratpermintaan.core.domain.entities.responses.PersyaratanItemDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.animations.SlideAnimation

class EditItemSuratPermintaanAdapter(): RecyclerView.Adapter<EditItemSuratPermintaanAdapter.ViewHolder>() {

    companion object {
        val SPA = 0
        val SPB = 1
        val SPS = 2
    }

    val itemList: ArrayList<ItemsDetailSP> = arrayListOf()
    val viewType: ArrayList<String> = arrayListOf()
    val persyaratanList = mutableMapOf<String, String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditItemSuratPermintaanAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SPA -> ViewHolderSPA(inflater.inflate(R.layout.detail_history_spa_item, parent, false))
            SPB -> ViewHolderSPB(inflater.inflate(R.layout.detail_history_spb_item, parent, false))
            else -> ViewHolderSPS(inflater.inflate(R.layout.detail_history_sps_item, parent, false))
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: EditItemSuratPermintaanAdapter.ViewHolder, position: Int) {
        val data = itemList.get(position)
        when (getItemViewType(position)) {
            SPA -> {
                val viewHolderSPA = holder as ViewHolderSPA

                if (data.tombolEditItem == 1) {
                    viewHolderSPA.btnEditSPA.visibility = View.VISIBLE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPA.btnHapusSPA.visibility = View.VISIBLE
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

                viewHolderSPA.expandedParentSPA.setOnClickListener {
                    if (viewHolderSPA.expandedChildSPA.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPA.cardViewSPA,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPA.expandedChildSPA.visibility = View.VISIBLE
                        viewHolderSPA.imageExpandedParentSPA.setImageDrawable(
                            viewHolderSPA.imageExpandedParentSPA.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPA.expandedChildSPA)

                        viewHolderSPA.expandedChildSPA.animation = animation
                        viewHolderSPA.expandedChildSPA.startAnimation(animation)
                        viewHolderSPA.imageExpandedParentSPA.setImageDrawable(
                            viewHolderSPA.imageExpandedParentSPA.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }

                }
            }

            SPB -> {
                val viewHolderSPB = holder as ViewHolderSPB

                if (data.tombolEditItem == 1) {
                    viewHolderSPB.btnEditSPB.visibility = View.VISIBLE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPB.btnHapusSPB.visibility = View.VISIBLE
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

                viewHolderSPB.expandedParentSPB.setOnClickListener {
                    if (viewHolderSPB.expandedChildSPB.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPB.cardViewSPB,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPB.expandedChildSPB.visibility = View.VISIBLE
                        viewHolderSPB.imageExpandedParentSPB.setImageDrawable(
                            viewHolderSPB.imageExpandedParentSPB.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPB.expandedChildSPB)

                        viewHolderSPB.expandedChildSPB.animation = animation
                        viewHolderSPB.expandedChildSPB.startAnimation(animation)
                        viewHolderSPB.imageExpandedParentSPB.setImageDrawable(
                            viewHolderSPB.imageExpandedParentSPB.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }
                }
            }

            SPS -> {
                val viewHolderSPS = holder as ViewHolderSPS

                if (data.tombolEditItem == 1) {
                    viewHolderSPS.btnEditSPS.visibility = View.VISIBLE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPS.btnHapusSPS.visibility = View.VISIBLE
                }

                viewHolderSPS.jenisDetailSPS.text = data.idBarang
                viewHolderSPS.kodeDetailSPS.text = data.kodePekerjaan
                viewHolderSPS.satuanDetailSPS.text = data.idSatuan
                viewHolderSPS.waktuDetailSPS.text = data.waktuPelaksanaan
                viewHolderSPS.qtyDetailSPS.text = data.qty
                var syarat: String? = ""

                data.persyaratan?.forEach {
                    val dataSyarat = it as PersyaratanItemDetailSP
                    syarat += "${persyaratanList[dataSyarat.persyaratan]}\n"
                }
                viewHolderSPS.persyaratanDetailSPS.text = syarat


                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPS.keteranganDetailSPS.text = dataKeterangan

                viewHolderSPS.expandedParentSPS.setOnClickListener {
                    if (viewHolderSPS.expandedChildSPS.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPS.cardViewSPS,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPS.expandedChildSPS.visibility = View.VISIBLE
                        viewHolderSPS.imageExpandedParentSPS.setImageDrawable(
                            viewHolderSPS.imageExpandedParentSPS.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPS.expandedChildSPS)

                        viewHolderSPS.expandedChildSPS.animation = animation
                        viewHolderSPS.expandedChildSPS.startAnimation(animation)
                        viewHolderSPS.imageExpandedParentSPS.setImageDrawable(
                            viewHolderSPS.imageExpandedParentSPS.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (viewType[position].equals("SPA")) {
            return SPA
        } else if (viewType[position].equals("SPB")) {
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
        val expandedChildSPA: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPA)
        val expandedParentSPA: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPA)
        val cardViewSPA: CardView = itemView.findViewById(R.id.cardViewSPA)
        val imageExpandedParentSPA: ImageView = itemView.findViewById(R.id.imgExpandedParentSPA)
        val btnEditSPA: Button = itemView.findViewById(R.id.btnEditSPA)
        val btnHapusSPA: Button = itemView.findViewById(R.id.btnHapusSPA)
    }

    inner class ViewHolderSPB(itemView: View) : ViewHolder(itemView) {
        val jenisDetailSPB: TextView = itemView.findViewById(R.id.tvJenisDetailSPB)
        val kodeDetailSPB: TextView = itemView.findViewById(R.id.tvKodeDetailSPB)
        val satuanDetailSPB: TextView = itemView.findViewById(R.id.tvSatuanDetailSPB)
        val fungsiDetailSPB: TextView = itemView.findViewById(R.id.tvFungsiDetailSPB)
        val targetDetailSPB: TextView = itemView.findViewById(R.id.tvTargetDetailSPB)
        val qtyDetailSPB: TextView = itemView.findViewById(R.id.tvQtySPB)
        val keteranganDetailSPB: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPB)
        val expandedChildSPB: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPB)
        val expandedParentSPB: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPB)
        val cardViewSPB: CardView = itemView.findViewById(R.id.cardViewSPB)
        val imageExpandedParentSPB: ImageView = itemView.findViewById(R.id.imgExpandedParentSPB)
        val btnEditSPB: Button = itemView.findViewById(R.id.btnEditSPB)
        val btnHapusSPB: Button = itemView.findViewById(R.id.btnHapusSPB)

    }

    inner class ViewHolderSPS(itemView: View) : ViewHolder(itemView) {
        val jenisDetailSPS: TextView = itemView.findViewById(R.id.tvJenisDetailSPS)
        val kodeDetailSPS: TextView = itemView.findViewById(R.id.tvKodeDetailSPS)
        val satuanDetailSPS: TextView = itemView.findViewById(R.id.tvSatuanDetailSPS)
        val persyaratanDetailSPS: TextView = itemView.findViewById(R.id.tvPersyaratanDetailSPS)
        val waktuDetailSPS: TextView = itemView.findViewById(R.id.tvWaktuDetailSPS)
        val qtyDetailSPS: TextView = itemView.findViewById(R.id.tvQtySPS)
        val keteranganDetailSPS: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPS)
        val expandedChildSPS: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPS)
        val expandedParentSPS: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPS)
        val cardViewSPS: CardView = itemView.findViewById(R.id.cardViewSPS)
        val imageExpandedParentSPS: ImageView = itemView.findViewById(R.id.imgExpandedParentSPS)
        val btnEditSPS: Button = itemView.findViewById(R.id.btnEditSPS)
        val btnHapusSPS: Button = itemView.findViewById(R.id.btnHapusSPS)
    }

    private fun animation(expanded: ConstraintLayout): Animation {
        val animation: Animation =
            SlideAnimation(
                expanded,
                expanded.height,
                0
            )

        animation.interpolator = DecelerateInterpolator()
        animation.duration = 150
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                animation?.setAnimationListener(null)
                expanded.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        return animation
    }
}