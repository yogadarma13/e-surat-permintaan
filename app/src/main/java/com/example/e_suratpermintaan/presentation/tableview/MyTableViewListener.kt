package com.example.e_suratpermintaan.presentation.tableview

import android.util.Log
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

import com.evrencoskun.tableview.ITableView

import com.evrencoskun.tableview.listener.ITableViewListener
import com.example.e_suratpermintaan.presentation.tableview.holder.ColumnHeaderViewHolder
import com.example.e_suratpermintaan.presentation.tableview.popup.ColumnHeaderLongPressPopup


class MyTableViewListener(private val mTableView: ITableView) : ITableViewListener {
    override fun onCellClicked(
        @NonNull cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
        Log.d(
            LOG_TAG,
            "onCellClicked has been clicked for x= $column y= $row"
        )
    }

    override fun onColumnHeaderDoubleClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {

    }

    override fun onCellDoubleClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {

    }

    override fun onCellLongPressed(
        @NonNull cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
        Log.d(
            LOG_TAG,
            "onCellLongPressed has been clicked for $row"
        )
    }

    override fun onColumnHeaderClicked(
        @NonNull columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
        Log.d(
            LOG_TAG,
            "onColumnHeaderClicked has been clicked for $column"
        )
    }

    override fun onColumnHeaderLongPressed(
        @NonNull columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
        if (columnHeaderView != null && columnHeaderView is ColumnHeaderViewHolder) {

            // Create Long Press Popup
            val popup = ColumnHeaderLongPressPopup(
                columnHeaderView, mTableView
            )

            // Show
            popup.show()
        }
    }

    override fun onRowHeaderClicked(
        @NonNull rowHeaderView: RecyclerView.ViewHolder,
        row: Int
    ) {
        Log.d(
            LOG_TAG,
            "onRowHeaderClicked has been clicked for $row"
        )
    }

    override fun onRowHeaderLongPressed(
        @NonNull owHeaderView: RecyclerView.ViewHolder,
        row: Int
    ) {
        Log.d(
            LOG_TAG,
            "onRowHeaderLongPressed has been clicked for $row"
        )
    }

    override fun onRowHeaderDoubleClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        private val LOG_TAG = MyTableViewListener::class.java.simpleName
    }

}