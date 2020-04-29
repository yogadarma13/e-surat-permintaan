package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.DetailHistoryViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileDownloadViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_history.*

class DetailHistoryActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_detail_history
    private var dataDetailHistory: String? = null
    private lateinit var detailHistoryBaruAdapter: BaseAdapter<DetailHistoryViewHolder>
    private lateinit var detailHistoryLamaAdapter: BaseAdapter<DetailHistoryViewHolder>
    private lateinit var fileDownloadBaruAdapter: BaseAdapter<FileDownloadViewHolder>
    private lateinit var fileDownloadLamaAdapter: BaseAdapter<FileDownloadViewHolder>
    private lateinit var data: List<DetailHistory>
    private lateinit var dataBaru: DetailHistory
    private lateinit var dataLama: DetailHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataDetailHistory = intent.getStringExtra("detail_history")

        data = Gson().fromJson(dataDetailHistory, Array<DetailHistory>::class.java).toList()
        dataBaru = data.get(0)
        dataLama = data.get(1)

        init()
    }

    private fun init() {
        detailHistoryBaruAdapter = BaseAdapter(
            R.layout.item_detail_history, DetailHistoryViewHolder::class.java
        )
        detailHistoryLamaAdapter = BaseAdapter(
            R.layout.item_detail_history, DetailHistoryViewHolder::class.java
        )
        fileDownloadBaruAdapter = BaseAdapter(
            R.layout.file_download_item, FileDownloadViewHolder::class.java
        )
        fileDownloadLamaAdapter = BaseAdapter(
            R.layout.file_download_item, FileDownloadViewHolder::class.java
        )

//        setupListeners()
        initRecyclerView()
        setDataDetailHistory()
    }

    private fun initRecyclerView() {

        recyclerViewBaru.layoutManager = LinearLayoutManager(this)

        recyclerViewBaru.adapter = detailHistoryBaruAdapter

        recyclerViewLama.layoutManager = LinearLayoutManager(this)
        recyclerViewLama.adapter = detailHistoryLamaAdapter

        recyclerViewButtonDownloadBaru.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewButtonDownloadBaru.adapter = fileDownloadBaruAdapter

        recyclerViewButtonDownloadLama.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewButtonDownloadLama.adapter = fileDownloadLamaAdapter
    }

//    private fun setupListeners() {
//        fileDownloadBaruAdapter.setOnItemClickListener {
//            val dataFileBaru = it as FilesDetailHistory
//            toastNotify(dataFileBaru.dir)
//        }
//
//        fileDownloadLamaAdapter.setOnItemClickListener {
//            val dataFileLama = it as FilesDetailHistory
//            toastNotify(dataFileLama.dir)
//        }
//    }

    private fun setDataDetailHistory() {

        tvTextBaru.text = dataBaru.text
        tvTextLama.text = dataLama.text

        dataBaru.items?.forEach {
            it?.let { it1 -> detailHistoryBaruAdapter.itemList.add(it1) }
        }
        detailHistoryBaruAdapter.notifyDataSetChanged()

        dataLama.items?.forEach {
            it?.let { it1 -> detailHistoryLamaAdapter.itemList.add(it1) }
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
