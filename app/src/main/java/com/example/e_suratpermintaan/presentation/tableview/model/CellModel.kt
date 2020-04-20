package com.example.e_suratpermintaan.presentation.tableview.model

import com.evrencoskun.tableview.sort.ISortableModel


class CellModel(private val mId: String, val data: String) : ISortableModel {

    override fun getId(): String {
        return mId
    }

    override fun getContent(): Any? {
        return data
    }

}