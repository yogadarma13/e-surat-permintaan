package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DataHistory
import com.e_suratpermintaan.core.domain.entities.responses.DetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.DetailHistoryViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_history.*
import org.json.JSONArray
import org.json.JSONObject

class DetailHistoryActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_detail_history
    private var dataDetailHistory: String? = null
    private lateinit var detailHistoryList: DetailHistory
    private lateinit var detailHistoryAdapter: BaseAdapter<DetailHistoryViewHolder>
    private lateinit var data: List<DetailHistory>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataDetailHistory = intent.getStringExtra("detail_history")

        data = Gson().fromJson(dataDetailHistory, Array<DetailHistory>::class.java).toList()

        init()
    }

    private fun init() {
        detailHistoryAdapter = BaseAdapter(
            R.layout.detail_history_item, DetailHistoryViewHolder::class.java
        )

        initRecyclerView()
        setDataDetailHistory()
    }

    private fun initRecyclerView() {

        recyclerViewBaru.layoutManager = LinearLayoutManager(this)
        recyclerViewBaru.adapter = detailHistoryAdapter

        recyclerViewLama.layoutManager = LinearLayoutManager(this)
        recyclerViewLama.adapter = detailHistoryAdapter
    }

    private fun setDataDetailHistory() {
//        val dataItemHistoryBaru = arrayListOf<ItemsDetailHistory>()
//        val dataItemHistoryLama = arrayListOf<ItemsDetailHistory>()

//        data.get(0)?.detail?.get(0).items

        data.get(0).items?.forEach {
            it?.let { it1 -> detailHistoryAdapter.itemList.add(it1) }
        }
        detailHistoryAdapter.notifyDataSetChanged()


        data.get(1).items?.forEach {
            it?.let { it1 -> detailHistoryAdapter.itemList.add(it1) }
        }

        detailHistoryAdapter.notifyDataSetChanged()
    }
}
