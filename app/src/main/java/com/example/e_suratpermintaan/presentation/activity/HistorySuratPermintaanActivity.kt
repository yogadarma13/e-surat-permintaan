package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.HistorySPResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.adapter.HistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history_surat_permintaan.recyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class HistorySuratPermintaanActivity : BaseActivity() {

    private val historyViewModel: SuratPermintaanViewModel by viewModel()

    private lateinit var historyAdapter: HistoryAdapter
    private var idSp : String? = null

    override fun layoutId(): Int = R.layout.activity_history_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString("id_sp")

        init()
    }

    private fun init() {
        historyAdapter = HistoryAdapter()
        initRecyclerView()
        getHistory()
        setupListener()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = historyAdapter
    }

    private fun setupListener() {
        historyAdapter.setOnClickListener(object :
        HistoryAdapter.OnClickItemListener {
            override fun onClick(view: View, item: List<DetailHistory?>?) {
                val gson = Gson()
                val json = gson.toJson(item)
                val intent = Intent(this@HistorySuratPermintaanActivity, DetailHistoryActivity::class.java)
                intent.putExtra("detail_history", json)
                startActivity(intent)
            }
        })
    }

    private fun getHistory() {
        disposable = historyViewModel.readHistory(idSp.toString())
            .subscribe(this::historyResponse, this::handleError)
    }

    private fun historyResponse(response: HistorySPResponse) {

        response.data?.let { historyAdapter.dataHistorySP.addAll(it) }
        historyAdapter.notifyDataSetChanged()
    }
}
