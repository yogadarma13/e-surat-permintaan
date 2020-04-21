package com.example.e_suratpermintaan.presentation.tableview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.tableview.holder.ActionCellViewHolder
import com.example.e_suratpermintaan.presentation.tableview.holder.CellViewHolder
import com.example.e_suratpermintaan.presentation.tableview.holder.ColumnHeaderViewHolder
import com.example.e_suratpermintaan.presentation.tableview.holder.RowHeaderViewHolder
import com.example.e_suratpermintaan.presentation.tableview.model.CellModel
import com.example.e_suratpermintaan.presentation.tableview.model.ColumnHeaderModel
import com.example.e_suratpermintaan.presentation.tableview.model.RowHeaderModel

class MyTableAdapter(val mContext: Context?) :
    AbstractTableAdapter<ColumnHeaderModel?, RowHeaderModel?, CellModel?>() {
    private val myTableViewModel: MyTableViewModel = MyTableViewModel()
    override fun onCreateCellViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {

        val layout: View
        return when (viewType) {
            MyTableViewModel.ACTION_TYPE -> {
                // Get money cell xml Layout
                layout = LayoutInflater.from(mContext)
                    .inflate(R.layout.tableview_cell_layout, parent, false)

                // Create the relevant view holder
                ActionCellViewHolder(layout)
            }
            else -> {
                // Get default Cell xml Layout
                layout = LayoutInflater.from(mContext).inflate(
                    R.layout.tableview_cell_layout,
                    parent, false
                )

                // Create a Cell ViewHolder
                CellViewHolder(layout)
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractSorterViewHolder {
        val layout: View = LayoutInflater.from(mContext)
            .inflate(R.layout.tableview_column_header_layout, parent, false)
        return ColumnHeaderViewHolder(layout, tableView)
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {

        // Get Row Header xml Layout
        val layout: View = LayoutInflater.from(mContext).inflate(
            R.layout.tableview_row_header_layout,
            parent, false
        )

        // Create a Row Header ViewHolder
        return RowHeaderViewHolder(layout)
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getCellItemViewType(position: Int): Int {
        return myTableViewModel.getCellItemViewType(position)
    }

    /**
     * This method is not a generic Adapter method. It helps to generate lists from single user
     * list for this adapter.
     */
    fun setDataList(userList: List<DataMyData?>?) {
        // Generate the lists that are used to TableViewAdapter
        myTableViewModel.generateListForTableView(userList as List<DataMyData>)

        // Now we got what we need to show on TableView.
        setAllItems(
            myTableViewModel.columHeaderModeList as List<ColumnHeaderModel>,
            myTableViewModel.rowHeaderModelList as List<RowHeaderModel>,
            myTableViewModel.cellModelList as List<List<CellModel>>
        )
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: CellModel?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell: CellModel? = cellItemModel
        if (holder is CellViewHolder) {
            // Get the holder to update cell item text
            holder.setCellModel(cell!!, columnPosition)
        }
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeaderModel?,
        columnPosition: Int
    ) {
        val columnHeader: ColumnHeaderModel? = columnHeaderItemModel

        // Get the holder to update cell item text
        val columnHeaderViewHolder: ColumnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.setColumnHeaderModel(columnHeader!!, columnPosition)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeaderModel?,
        rowPosition: Int
    ) {
        val rowHeaderModel: RowHeaderModel = rowHeaderItemModel as RowHeaderModel
        val rowHeaderViewHolder: RowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.row_header_textview.text = rowHeaderModel.data
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false)
    }
}