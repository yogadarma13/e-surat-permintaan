package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.HistorySPResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityHistorySuratPermintaanBinding
import com.example.e_suratpermintaan.presentation.activity.DetailHistoryActivity.Companion.DETAIL_HISTORY_SP
import com.example.e_suratpermintaan.presentation.activity.DetailHistoryActivity.Companion.JENIS_SP_DETAIL_HISTORY
import com.example.e_suratpermintaan.presentation.adapter.HistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistorySuratPermintaanActivity : BaseActivity<ActivityHistorySuratPermintaanBinding>() {

    companion object {
        const val ID_SP_HISTORY = "id_sp_history"
        const val JENIS_SP_HISTORY = "jenis_sp_history"
    }

    private val historyViewModel: SuratPermintaanViewModel by viewModel()

    private lateinit var historyAdapter: HistoryAdapter
    private var idSp: String? = null
    private var jenisSp: String? = null

    override fun getViewBinding(): ActivityHistorySuratPermintaanBinding =
        ActivityHistorySuratPermintaanBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_HISTORY)
        jenisSp = intent.extras?.getString(JENIS_SP_HISTORY)

        init()
    }

    private fun init() {
        setupTollbar()
        historyAdapter = HistoryAdapter()
        initRecyclerView()
        setupListener()
        showSwipeRefreshLayout()
        getHistory()
    }

    private fun setupTollbar() {
        binding.toolbarHistory.text = getString(R.string.toolbar_history)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(
                this@HistorySuratPermintaanActivity,
                RecyclerView.VERTICAL,
                false
            )
            adapter = historyAdapter
        }
    }

    private fun setupListener() {
        historyAdapter.onItemClick = { list ->
            val gson = Gson()
            val json = gson.toJson(list)
            val intent =
                Intent(this@HistorySuratPermintaanActivity, DetailHistoryActivity::class.java)
            intent.putExtra(DETAIL_HISTORY_SP, json)
            intent.putExtra(JENIS_SP_DETAIL_HISTORY, jenisSp)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this::getHistory)
    }

    private fun getHistory() {
        disposable = historyViewModel.readHistory(idSp.toString())
            .subscribe(this::historyResponse, this::handleError)
    }

    private fun showSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun dismissSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun historyResponse(response: HistorySPResponse) {
        historyAdapter.dataHistorySP.clear()
        response.data?.let { historyAdapter.dataHistorySP.addAll(it) }
        historyAdapter.notifyDataSetChanged()
        dismissSwipeRefreshLayout()
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        dismissSwipeRefreshLayout()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
