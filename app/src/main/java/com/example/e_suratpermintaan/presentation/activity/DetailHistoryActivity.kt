package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.FilesDetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.MasterPersyaratanResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.utils.Directory
import com.example.e_suratpermintaan.framework.utils.DownloadTask
import com.example.e_suratpermintaan.framework.utils.FileName
import com.example.e_suratpermintaan.presentation.adapter.DetailHistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileDownloadViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHistoryActivity : BaseActivity() {
    
    companion object {
        const val DETAIL_HISTORY_SP = "detail_history_sp"
        const val JENIS_SP_DETAIL_HISTORY = "jenis_sp_detail_history"
    }

    override fun layoutId(): Int = R.layout.activity_detail_history

    private val masterViewModel: MasterViewModel by viewModel()

    private var dataDetailHistory: String? = null
    private var jenisSP: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private lateinit var detailHistoryBaruAdapter: DetailHistoryAdapter
    private lateinit var detailHistoryLamaAdapter: DetailHistoryAdapter
    private lateinit var fileDownloadBaruAdapter: BaseAdapter<FileDownloadViewHolder>
    private lateinit var fileDownloadLamaAdapter: BaseAdapter<FileDownloadViewHolder>
    private lateinit var data: List<DetailHistory>
    private lateinit var dataBaru: DetailHistory
    private lateinit var dataLama: DetailHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataDetailHistory = intent.getStringExtra(DETAIL_HISTORY_SP)
        jenisSP = intent.getStringExtra(JENIS_SP_DETAIL_HISTORY)

        data = Gson().fromJson(dataDetailHistory, Array<DetailHistory>::class.java).toList()
        dataBaru = data.get(0)
        dataLama = data.get(1)

        init()
    }

    private fun init() {
//        detailHistoryBaruAdapter = BaseAdapter(
//            R.layout.detail_history_spb_item, DetailHistoryViewHolder::class.java
//        )
//        detailHistoryLamaAdapter = BaseAdapter(
//            R.layout.detail_history_spb_item, DetailHistoryViewHolder::class.java
//        )
        detailHistoryBaruAdapter = DetailHistoryAdapter()
        detailHistoryLamaAdapter = DetailHistoryAdapter()

        fileDownloadBaruAdapter = BaseAdapter(
            R.layout.file_download_item, FileDownloadViewHolder::class.java
        )
        fileDownloadLamaAdapter = BaseAdapter(
            R.layout.file_download_item, FileDownloadViewHolder::class.java
        )

        disposable = masterViewModel.getPersyaratanList("all")
            .subscribe(this::handleResponse, this::handleError)

        setupTollbar()
        setupListeners()
        initRecyclerView()
    }

    private fun setupTollbar() {
        if (toolbar_detail_history != null && toolbar != null) {
            toolbar_detail_history.text = getString(R.string.toolbar_detail_history)
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayShowTitleEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleResponse(response: MasterPersyaratanResponse) {
        val dataPersyaratan = response.data

        dataPersyaratan?.forEach {
            persyaratanList[it?.id.toString()] = it?.nama.toString()
        }

        setDataDetailHistory()
    }

    private fun initRecyclerView() {

//        recyclerViewBaru.itemAnimator = null
        recyclerViewBaru.layoutManager = LinearLayoutManager(this)
        recyclerViewBaru.adapter = detailHistoryBaruAdapter

//        recyclerViewLama.itemAnimator = null
        recyclerViewLama.layoutManager = LinearLayoutManager(this)
        recyclerViewLama.adapter = detailHistoryLamaAdapter

        recyclerViewButtonDownloadBaru.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewButtonDownloadBaru.adapter = fileDownloadBaruAdapter

        recyclerViewButtonDownloadLama.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewButtonDownloadLama.adapter = fileDownloadLamaAdapter
    }

    private fun setupListeners() {
        fileDownloadBaruAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FilesDetailHistory

            when(actionString) {
                FileDownloadViewHolder.BTN_FILE -> {
                    val fileName = FileName.getFileNameFromURL(data.dir.toString())
                    if (Directory.checkDirectoryAndFileExists(this, fileName)) {
                        val downloadTask = DownloadTask(this, fileName)
                        downloadTask.execute(data.dir)
                    }
                }
            }
        }

        fileDownloadLamaAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FilesDetailHistory

            when(actionString) {
                FileDownloadViewHolder.BTN_FILE -> {
                    val fileName = FileName.getFileNameFromURL(data.dir.toString())
                    if (Directory.checkDirectoryAndFileExists(this, fileName)) {
                        val downloadTask = DownloadTask(this, fileName)
                        downloadTask.execute(data.dir)
                    }
                }
            }
        }

    }

    private fun setDataDetailHistory() {

        tvTextBaru.text = dataBaru.text
        tvTextLama.text = dataLama.text

        if (dataBaru.items?.size != 0) {
            detailHistoryBaruAdapter.persyaratanList.putAll(persyaratanList)
            dataBaru.items?.forEach {
                it?.let { it1 ->
                    detailHistoryBaruAdapter.historyList.add(it1)
                    detailHistoryBaruAdapter.viewType.add(jenisSP.toString())
                }
            }
        } else{
            tvDataBaruNull.visibility = View.VISIBLE
        }

        detailHistoryBaruAdapter.notifyDataSetChanged()

        if (dataLama.items?.size != 0) {
            detailHistoryLamaAdapter.persyaratanList.putAll(persyaratanList)
            dataLama.items?.forEach {
                it?.let { it1 ->
                    detailHistoryLamaAdapter.historyList.add(it1)
                    detailHistoryLamaAdapter.viewType.add(jenisSP.toString())
                }
            }
        } else {
            tvDataLamaNull.visibility = View.VISIBLE
        }
        detailHistoryLamaAdapter.notifyDataSetChanged()

        dataBaru.files?.forEach {
            it?.let { it1 -> fileDownloadBaruAdapter.itemList.add(it1) }
        }
        fileDownloadBaruAdapter.notifyDataSetChanged()

        dataLama.files?.forEach {
            it?.let { it1 -> fileDownloadLamaAdapter.itemList.add(it1) }
        }
        fileDownloadLamaAdapter.notifyDataSetChanged()
    }
}
