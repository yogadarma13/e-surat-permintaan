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
import com.example.e_suratpermintaan.presentation.activity.DetailHistoryActivity.Companion.DETAIL_HISTORY_SP
import com.example.e_suratpermintaan.presentation.activity.DetailHistoryActivity.Companion.JENIS_SP_DETAIL_HISTORY
import com.example.e_suratpermintaan.presentation.adapter.HistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history_surat_permintaan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class HistorySuratPermintaanActivity : BaseActivity() {

    companion object {
        const val ID_SP_HISTORY = "id_sp_history"
        const val JENIS_SP_HISTORY = "jenis_sp_history"
    }

    private val historyViewModel: SuratPermintaanViewModel by viewModel()

    private lateinit var historyAdapter: HistoryAdapter
    private var idSp : String? = null
    private var jenisSp : String? = null

    override fun layoutId(): Int = R.layout.activity_history_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (toolbar_history != null && toolbar != null) {
            toolbar_history.text = getString(R.string.toolbar_history)
            setSupportActionBar(toolbar)
        }

        idSp = intent.extras?.getString(ID_SP_HISTORY)
        jenisSp = intent.extras?.getString(JENIS_SP_HISTORY)

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
                intent.putExtra(DETAIL_HISTORY_SP, json)
                intent.putExtra(JENIS_SP_DETAIL_HISTORY, jenisSp)
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
