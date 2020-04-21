package com.example.e_suratpermintaan.presentation.tableview

import android.view.Gravity
import com.e_suratpermintaan.core.domain.entities.responses.DataAll
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.example.e_suratpermintaan.presentation.tableview.model.CellModel
import com.example.e_suratpermintaan.presentation.tableview.model.ColumnHeaderModel
import com.example.e_suratpermintaan.presentation.tableview.model.RowHeaderModel

class MyTableViewModel {
    private var mColumnHeaderModelList: List<ColumnHeaderModel>? = null
    private var mRowHeaderModelList: List<RowHeaderModel>? = null
    private var mCellModelList: List<List<CellModel>>? = null
    fun getCellItemViewType(column: Int): Int {
        return when (column) {
            6 -> ACTION_TYPE // 6 itu index bukan posisi
            else -> 0
        }
    }

    /*
       - Each of Column Header -
        "Kode"
        "Tanggal Pengajuan"
        "Proyek"
        "Lokasi"
        "Status Permintaan"
        "Jenis"
        "Action"
     */
    fun getColumnTextAlign(column: Int): Int {
        return when (column) {
            0 -> Gravity.CENTER
            1 -> Gravity.LEFT
            2 -> Gravity.LEFT
            3 -> Gravity.LEFT
            4 -> Gravity.CENTER
            5 -> Gravity.CENTER
            6 -> Gravity.CENTER
            else -> Gravity.CENTER
        }
    }

    private fun createColumnHeaderModelList(): List<ColumnHeaderModel> {
        val list: MutableList<ColumnHeaderModel> = ArrayList()

        // Create Column Headers
        list.add(ColumnHeaderModel("Kode"))
        list.add(ColumnHeaderModel("Tanggal Pengajuan"))
        list.add(ColumnHeaderModel("Proyek"))
        list.add(ColumnHeaderModel("Lokasi"))
        list.add(ColumnHeaderModel("Status Permintaan"))
        list.add(ColumnHeaderModel("Jenis"))
        list.add(ColumnHeaderModel("Action"))
        return list
    }

    private fun createCellModelList(dataList: List<DataAll>): List<List<CellModel>> {
        val lists: MutableList<List<CellModel>> = ArrayList()

        // Creating cell model list from _root_ide_package_.com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMyData list for Cell Items
        // In this example, _root_ide_package_.com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMyData list is populated from web service
        for (i in dataList.indices) {
            val data: DataAll = dataList[i]
            val list: MutableList<CellModel> = ArrayList()

            // The order should be same with column header list;
            list.add(CellModel("1-$i", data.kode.toString()))
            list.add(CellModel("2-$i", data.tanggalPengajuan.toString()))
            list.add(CellModel("3-$i", data.namaProyek.toString()))
            list.add(CellModel("4-$i", data.namaLokasi.toString()))
            list.add(CellModel("5-$i", data.statusPermintaan.toString()))
            list.add(CellModel("6-$i", data.jenis.toString()))
            list.add(CellModel("7-$i", "aksi"))

            // Add
            lists.add(list)
        }
        return lists
    }

    private fun createRowHeaderList(size: Int): List<RowHeaderModel> {
        val list: MutableList<RowHeaderModel> = ArrayList()
        for (i in 0 until size) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(RowHeaderModel((i + 1).toString()))
        }
        return list
    }

    val columHeaderModeList: List<Any>?
        get() = mColumnHeaderModelList

    val rowHeaderModelList: List<Any>?
        get() = mRowHeaderModelList

    val cellModelList: List<List<Any>>?
        get() = mCellModelList

    fun generateListForTableView(users: List<DataAll>) {
        mColumnHeaderModelList = createColumnHeaderModelList()
        mCellModelList = createCellModelList(users)
        mRowHeaderModelList = createRowHeaderList(users.size)
    }

    companion object {
        // View Types
        const val ACTION_TYPE = 1
    }
}
