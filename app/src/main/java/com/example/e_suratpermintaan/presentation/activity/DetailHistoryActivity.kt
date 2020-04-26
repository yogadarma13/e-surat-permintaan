package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DataHistory
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.adapter.DetailHistoryAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_history.*
import org.json.JSONArray
import org.json.JSONObject

class DetailHistoryActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_detail_history
    private var dataDetailHistory: String? = null
    private lateinit var detailHistoryList: DetailHistory
    private lateinit var detailHistoryAdapter: DetailHistoryAdapter
    private lateinit var data: List<DetailHistory>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataDetailHistory = intent.getStringExtra("detail_history")

        data = Gson().fromJson(dataDetailHistory, Array<DetailHistory>::class.java).toList()

        init()
    }

    private fun init() {
        initRecyclerView()
        setDataDetailHistory()
    }

    private fun initRecyclerView() {

        recyclerViewBaru.layoutManager = LinearLayoutManager(this)
        (recyclerViewBaru.layoutManager as LinearLayoutManager).isAutoMeasureEnabled = true
        recyclerViewBaru.setHasFixedSize(false)

        recyclerViewLama.layoutManager = LinearLayoutManager(this)
        (recyclerViewLama.layoutManager as LinearLayoutManager).isAutoMeasureEnabled = true
        recyclerViewLama.setHasFixedSize(false)
    }



    private fun setDataDetailHistory() {
        val dataItemHistoryBaru = arrayListOf<ItemsDetailHistory>()
        val dataItemHistoryLama = arrayListOf<ItemsDetailHistory>()

//        data.get(0)?.detail?.get(0).items

        data.get(0).items?.forEach {
            it?.let { it1 -> dataItemHistoryBaru.add(it1) }
        }

        data.get(1).items?.forEach {
            it?.let { it1 -> dataItemHistoryLama.add(it1) }
        }

        detailHistoryAdapter = DetailHistoryAdapter(dataItemHistoryBaru)
        detailHistoryAdapter.notifyDataSetChanged()
        recyclerViewBaru.adapter = detailHistoryAdapter

        detailHistoryAdapter = DetailHistoryAdapter(dataItemHistoryLama)
        detailHistoryAdapter.notifyDataSetChanged()
        recyclerViewLama.adapter = detailHistoryAdapter
    }
}
