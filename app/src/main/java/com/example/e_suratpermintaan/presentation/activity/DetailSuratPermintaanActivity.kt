package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.DetailSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.viewholders.CCViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.ItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.JenisBarangViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.UomViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import kotlinx.android.synthetic.main.dialog_tambah_item.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailSuratPermintaanActivity : BaseActivity() {

    private lateinit var alertDialogTambah: AlertDialog
    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedViewModel: SharedViewModel by inject()

    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder>

    private lateinit var dialogRootView: View

    private var idSp: String? = null
    private var kodeSp: String? = null
    private lateinit var idUser: String
    private lateinit var itemSuratPermintaanAdapter: BaseAdapter<ItemSuratPermintaanViewHolder>

    override fun layoutId(): Int = R.layout.activity_detail_surat_permintaan

    companion object {
        const val ID_SP_EXTRA_KEY = "id_sp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_EXTRA_KEY)

        init()

        val detailSP = DetailSP(idSp.toString(), idUser)
        disposable = suratPermintaanViewModel.readDetail(detailSP)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun init() {
        val profileId = profilePreference.getProfile()?.id
        if (profileId != null) {
            idUser = profileId
        }

        initRecyclerView()
        setupListeners()

        initDialogViewTambah()
    }

    private fun setupListeners() {
        tvAddItem.setOnClickListener {
            alertDialogTambah.show()
        }
    }

    private fun initRecyclerView() {

        itemSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_row,
            ItemSuratPermintaanViewHolder::class.java
        )

        itemSuratPermintaanAdapter.setOnItemClickListener {
            val item = it as ItemsDetailSP
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemSuratPermintaanAdapter

    }

    private fun handleResponse(response: Any) {
        when (response) {
            is CreateItemSPResponse -> {
                toastNotify(response.message)
            }
            is DetailSPResponse -> {

                val detailSPResponse = response.data
                val dataDetailSP = detailSPResponse?.get(0)
                kodeSp = dataDetailSP?.kode

                val detailDate = dataDetailSP?.tanggalPengajuan?.split(" ")

                tv_kode_detail.text = kodeSp
                tv_kode_sp.text = kodeSp
                tv_name_proyek_detail.text = dataDetailSP?.namaProyek
                tv_location_detail.text = dataDetailSP?.namaLokasi
                tv_date_detail.text = detailDate?.get(0)
                tv_time_detail.text = detailDate?.get(1)
                tv_status_detail.text = dataDetailSP?.statusPermintaan
                tv_jenis_detail.text = dataDetailSP?.jenis

                val itemList: List<ItemsDetailSP?>? = dataDetailSP?.items
                val itemArrayList: ArrayList<ItemsDetailSP?> = arrayListOf()

                itemList?.forEach {
                    itemArrayList.add(it)
                }

                itemSuratPermintaanAdapter.notifyDataSetChanged()

            }
        }
    }

    private fun handleError(error: Throwable) {
        toastNotify(error.message)
    }

    private fun initDialogViewTambah() {
        dialogRootView =
            this.layoutInflater.inflate(R.layout.dialog_tambah_item, null)

        dialogRootView.etKodePekerjaan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etKodePekerjaan.isFocused) {
                    ccAdapter.filter.filter(s)
                    dialogRootView.rvKodePekerjaan.visibility = View.VISIBLE
                }
            }
        })

        dialogRootView.etJenisBarang.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                jenisBarangAdapter.filter.filter(s)
                dialogRootView.rvJenisBarang.visibility = View.VISIBLE
            }
        })

        dialogRootView.etSatuan.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                uomAdapter.filter.filter(s)
                dialogRootView.rvSatuan.visibility = View.VISIBLE
            }
        })

        dialogRootView.rvKodePekerjaan.visibility = View.GONE
        dialogRootView.rvJenisBarang.visibility = View.GONE
        dialogRootView.rvSatuan.visibility = View.GONE

        ccAdapter = BaseFilterableAdapter(R.layout.simple_item_row, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener {
            dialogRootView.etKodePekerjaan.setText((it as DataMasterCC).kodeCostcontrol)
            dialogRootView.rvKodePekerjaan.visibility = View.GONE
            closeKeyboard(dialogRootView.etKodePekerjaan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKodePekerjaan.layoutManager = LinearLayoutManager(this)
        dialogRootView.rvKodePekerjaan.adapter = ccAdapter

        jenisBarangAdapter =
            BaseFilterableAdapter(R.layout.simple_item_row, JenisBarangViewHolder::class.java)
        jenisBarangAdapter.setOnItemClickListener {
            dialogRootView.etJenisBarang.setText((it as DataMasterCC).deskripsi)
            dialogRootView.rvJenisBarang.visibility = View.GONE
            closeKeyboard(dialogRootView.etJenisBarang)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvJenisBarang.layoutManager = LinearLayoutManager(this)
        dialogRootView.rvJenisBarang.adapter = jenisBarangAdapter

        uomAdapter = BaseFilterableAdapter(R.layout.simple_item_row, UomViewHolder::class.java)
        uomAdapter.setOnItemClickListener {
            dialogRootView.etSatuan.setText((it as DataMasterUOM).nama)
            dialogRootView.rvSatuan.visibility = View.GONE
            closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvSatuan.layoutManager = LinearLayoutManager(this)
        dialogRootView.rvSatuan.adapter = uomAdapter

        sharedViewModel.getCostCodeList().observe(this, Observer {
            it?.forEach { item ->
                ccAdapter.itemList.add(item as DataMasterCC)
                jenisBarangAdapter.itemList.add(item)
                Log.d("MYAPP", item.toString())
            }
            ccAdapter.oldItemList = ccAdapter.itemList
            ccAdapter.notifyDataSetChanged()

            jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
            jenisBarangAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getUomList().observe(this, Observer {
            it?.forEach { item ->
                uomAdapter.itemList.add(item as DataMasterUOM)
            }
            uomAdapter.oldItemList = uomAdapter.itemList
            uomAdapter.notifyDataSetChanged()
        })

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Tambah Item")
        alertDialogTambah = alertDialogBuilder.create()

        dialogRootView.btnTambah.setOnClickListener {
            val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
            val jenisBarang = dialogRootView.etJenisBarang.text.toString()
            val volume = dialogRootView.etVolume.text.toString()
            val satuan = dialogRootView.etSatuan.text.toString()
            val kapasitas = dialogRootView.etKapasistas.text.toString()
            val merk = dialogRootView.etMerk.text.toString()
            val waktuPemakaian = dialogRootView.etWaktuPemakaian.text.toString()

            alertDialogTambah.hide()
            val newAlertDialog = alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menambah item?")
                .setPositiveButton("Ya") { _, _ ->
                    val createItemSP = CreateItemSP(
                        kodeSp.toString(),
                        dialogRootView.etKodePekerjaan.text.toString(),
                        dialogRootView.etJenisBarang.text.toString(),
                        dialogRootView.etSatuan.text.toString(),
                        "50",
                        "",
                        "",
                        "",
                        dialogRootView.etKapasistas.text.toString(),
                        "",
                        "",
                        "2020-04-20",
                        arrayListOf("1", "2"),
                        idUser
                    )
                    disposable = itemSuratPermintaanViewModel.addItem(createItemSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogTambah.hide()

                }.create()

            newAlertDialog.show()
        }

        alertDialogTambah.setView(dialogRootView)
    }

}
