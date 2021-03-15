package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.FilesDetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.MasterPersyaratanResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityDetailHistoryBinding
import com.example.e_suratpermintaan.databinding.FileDownloadItemBinding
import com.example.e_suratpermintaan.framework.utils.Directory
import com.example.e_suratpermintaan.framework.utils.DownloadTask
import com.example.e_suratpermintaan.framework.utils.FileName
import com.example.e_suratpermintaan.presentation.adapter.DetailHistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileDownloadViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHistoryActivity : BaseActivity<ActivityDetailHistoryBinding>() {

    companion object {
        const val DETAIL_HISTORY_SP = "detail_history_sp"
        const val JENIS_SP_DETAIL_HISTORY = "jenis_sp_detail_history"
    }

    override fun getViewBinding(): ActivityDetailHistoryBinding =
        ActivityDetailHistoryBinding.inflate(layoutInflater)

    private val masterViewModel: MasterViewModel by viewModel()

    private var dataDetailHistory: String? = null
    private var jenisSP: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private lateinit var detailHistoryBaruAdapter: DetailHistoryAdapter
    private lateinit var detailHistoryLamaAdapter: DetailHistoryAdapter
    private lateinit var fileDownloadBaruAdapter: BaseAdapter<FileDownloadViewHolder, FileDownloadItemBinding>
    private lateinit var fileDownloadLamaAdapter: BaseAdapter<FileDownloadViewHolder, FileDownloadItemBinding>
    private lateinit var data: List<DetailHistory>
    private lateinit var dataBaru: DetailHistory
    private lateinit var dataLama: DetailHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataDetailHistory = intent.getStringExtra(DETAIL_HISTORY_SP)
        jenisSP = intent.getStringExtra(JENIS_SP_DETAIL_HISTORY)

        data = Gson().fromJson(dataDetailHistory, Array<DetailHistory>::class.java).toList()
        dataBaru = data[0]
        dataLama = data[1]

        init()
    }

    private fun init() {
        detailHistoryBaruAdapter = DetailHistoryAdapter()
        detailHistoryLamaAdapter = DetailHistoryAdapter()

        fileDownloadBaruAdapter = BaseAdapter(
            FileDownloadItemBinding::inflate, FileDownloadViewHolder::class.java
        )
        fileDownloadLamaAdapter = BaseAdapter(
            FileDownloadItemBinding::inflate, FileDownloadViewHolder::class.java
        )

        disposable = masterViewModel.getPersyaratanList("all")
            .subscribe(this::handleResponse, this::handleError)

        setupTollbar()
        setupListeners()
        initRecyclerView()
    }

    private fun setupTollbar() {
        binding.toolbarDetailHistory.text = getString(R.string.toolbar_detail_history)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

        with(binding.recyclerViewBaru) {
            layoutManager = LinearLayoutManager(this@DetailHistoryActivity)
            adapter = detailHistoryBaruAdapter
        }

        with(binding.recyclerViewLama) {
            layoutManager = LinearLayoutManager(this@DetailHistoryActivity)
            adapter = detailHistoryLamaAdapter
        }

        with(binding.recyclerViewButtonDownloadBaru) {
            layoutManager = LinearLayoutManager(
                this@DetailHistoryActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = fileDownloadBaruAdapter
        }

        with(binding.recyclerViewButtonDownloadLama) {
            layoutManager = LinearLayoutManager(
                this@DetailHistoryActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = fileDownloadLamaAdapter
        }
    }

    private fun setupListeners() {
        fileDownloadBaruAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FilesDetailHistory

            when (actionString) {
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

            when (actionString) {
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

        binding.tvTextBaru.text = dataBaru.text
        binding.tvTextLama.text = dataLama.text

        if (dataBaru.items?.size != 0) {
            detailHistoryBaruAdapter.persyaratanList.putAll(persyaratanList)
            dataBaru.items?.forEach {
                it?.let { it1 ->
                    detailHistoryBaruAdapter.historyList.add(it1)
                    detailHistoryBaruAdapter.viewType.add(jenisSP.toString())
                }
            }
        } else {
            binding.tvDataBaruNull.visibility = View.VISIBLE
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
            binding.tvDataLamaNull.visibility = View.VISIBLE
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
