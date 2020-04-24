package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.adapter.SuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private lateinit var idUser: String
    private lateinit var idProyek: String
    private lateinit var namaJenis: String

    private lateinit var proyekList: ArrayList<DataMasterProyek>
    private lateinit var jenisList: ArrayList<DataMasterJenis>

    private var spListState: Parcelable? = null
    private lateinit var suratPermintaanAdapter: SuratPermintaanAdapter

    private var isInitialized = false

    override fun layoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isInitialized) {
            isInitialized = true

            init()
        }
    }

    private fun init() {
        proyekList = arrayListOf()
        jenisList = arrayListOf()

        suratPermintaanAdapter = SuratPermintaanAdapter()
        setupListeners()
        initRecyclerView()

        val profileId = profilePreference.getProfile()?.id
        if (profileId != null) {
            idUser = profileId

            val spObservable = suratPermintaanViewModel.readMyData(profileId)
            val proyekObservable = masterViewModel.getProyekList(profileId)
            val jenisObservable = masterViewModel.getJenisList(profileId)

            disposable = spObservable.subscribe(this::handleResponse, this::handleError)
            disposable = proyekObservable.subscribe(this::handleResponse, this::handleError)
            disposable = jenisObservable.subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun initRecyclerView() {
        tv_show_length_entry.text = "Menampilkan ${suratPermintaanAdapter.spList.size} entri"

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = suratPermintaanAdapter

        (recyclerView.layoutManager as LinearLayoutManager).isAutoMeasureEnabled = true
        recyclerView.setHasFixedSize(false)

        if (spListState != null)
            recyclerView.layoutManager?.onRestoreInstanceState(spListState)
    }

    private fun setupListeners() {

        btnProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        btnAjukan.setOnClickListener {
            startShowDialog()
        }

        suratPermintaanAdapter.setOnClickListener(object :
            SuratPermintaanAdapter.OnClickItemListener {
            override fun onClick(view: View, item: SuratPermintaan?) {
                val intent = Intent(this@MainActivity, DetailSuratPermintaanActivity::class.java)
                intent.putExtra("id_sp", (item as DataMyData).id)
                startActivity(intent)
            }
        })
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is MyDataResponse -> {

                val suratPermintaanList: List<DataMyData?>? = response.data

                suratPermintaanList?.forEach {
                    suratPermintaanAdapter.spList.add(it)
                }

                tv_show_length_entry.text =
                    "Menampilkan ${suratPermintaanAdapter.spList.size} entri"
                suratPermintaanAdapter.notifyDataSetChanged()

            }
            is MasterProyekResponse -> {

                response.data?.forEach {
                    if (it != null) {
                        proyekList.add(it)
                    }
                }

            }
            is MasterJenisResponse -> {

                response.data?.forEach {
                    if (it != null) {
                        jenisList.add(it)
                    }
                }
            }
            is CreateSPResponse -> {

                toastNotify(response.message)

            }
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
    }

    private fun startShowDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Ajukan Surat Permintaan")

        var alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            this.layoutInflater.inflate(R.layout.dialog_ajukan_sp, null)

        val proyekAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, proyekList)
        val jenisAdapter = ArrayAdapter(this, R.layout.material_spinner_item, jenisList)

        dialogRootView.spinnerProyek.setAdapter(proyekAdapter)
        dialogRootView.spinnerJenis.setAdapter(jenisAdapter)
        dialogRootView.btnAjukan.setOnClickListener {
            val selectedProyek = dialogRootView.spinnerProyek.text.toString()
            val selectedJenis = dialogRootView.spinnerJenis.text.toString()

            idProyek = proyekList.find { it.nama == selectedProyek }?.id.toString()
            namaJenis = jenisList.find { it.nama == selectedJenis }?.nama.toString()

            alertDialog.hide()

            alertDialog = alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin mengajukan?")
                .setPositiveButton("Ya") { _, _ ->

//                    var createSP = CreateSP(idProyek, namaJenis, idUser)
                    disposable = suratPermintaanViewModel.add(idProyek, namaJenis, idUser)
                        .subscribe(this::handleResponse, this::handleError)

                    toastNotify("ID PROYEK : $idProyek \nNama Jenis : $namaJenis \nID USER : $idUser")

                    alertDialog.hide()

                }.create()

            alertDialog.show()
        }


        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }
}
