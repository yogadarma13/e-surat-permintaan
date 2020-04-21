package com.example.e_suratpermintaan.presentation.tableview.popup

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.sort.SortState
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.tableview.holder.ColumnHeaderViewHolder


class ColumnHeaderLongPressPopup(
    p_iViewHolder: ColumnHeaderViewHolder,
    p_jTableView: ITableView
) :
    PopupMenu(p_iViewHolder.itemView.context, p_iViewHolder.itemView),
    PopupMenu.OnMenuItemClickListener {
    private var m_iViewHolder: ColumnHeaderViewHolder? = p_iViewHolder
    private val m_iTableView: ITableView = p_jTableView
    private val mContext: Context = p_iViewHolder.itemView.context
    private val mXPosition: Int
    private fun initialize() {
        createMenuItem()
        changeMenuItemVisibility()
        this.setOnMenuItemClickListener(this)
    }

    private fun createMenuItem() {
        this.menu.add(
            Menu.NONE,
            ASCENDING,
            0,
            mContext.getString(R.string.sort_ascending)
        )
        this.menu.add(
            Menu.NONE,
            DESCENDING,
            1,
            mContext.getString(R.string.sort_descending)
        )
        this.menu.add(
            Menu.NONE,
            ROW_HIDE,
            2,
            mContext.getString(R.string.row_hide)
        )
        this.menu.add(
            Menu.NONE,
            ROW_SHOW,
            3,
            mContext.getString(R.string.row_show)
        )
        // add new one ...
    }

    private fun changeMenuItemVisibility() {
        // Determine which one shouldn't be visible
        val sortState = m_iTableView.getSortingStatus(mXPosition)
        if (sortState == SortState.UNSORTED) {
            // Show others
        } else if (sortState == SortState.DESCENDING) {
            // Hide DESCENDING menu item
            menu.getItem(1).isVisible = false
        } else if (sortState == SortState.ASCENDING) {
            // Hide ASCENDING menu item
            menu.getItem(0).isVisible = false
        }

        // Control whether 5. row is visible or not.
        if (m_iTableView.isRowVisible(TEST_ROW_INDEX)) {
            // Show row menu item will be invisible
            menu.getItem(3).isVisible = false
        } else {
            //  Hide row menu item will be invisible
            menu.getItem(2).isVisible = false
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        // Note: item id is index of menu item..
        when (menuItem.itemId) {
            ASCENDING -> m_iTableView.sortColumn(
                mXPosition,
                SortState.ASCENDING
            )
            DESCENDING -> m_iTableView.sortColumn(
                mXPosition,
                SortState.DESCENDING
            )
            ROW_HIDE ->                 // Hide 5. row for testing process
                // index starts from 0. That's why TEST_ROW_INDEX is 4.
                m_iTableView.hideRow(TEST_ROW_INDEX)
            ROW_SHOW ->                 // Show 5. row for testing process
                // index starts from 0. That's why TEST_ROW_INDEX is 4.
                m_iTableView.showRow(TEST_ROW_INDEX)
        }

        // Recalculate of the width values of the columns
        m_iTableView.remeasureColumnWidth(mXPosition)
        return true
    }

    companion object {
        private val LOG_TAG = ColumnHeaderLongPressPopup::class.java.simpleName

        // Sort states
        private const val ASCENDING = 1
        private const val DESCENDING = 2
        private const val CLEAR = 3

        // Test menu items for showing / hiding row
        private const val ROW_HIDE = 4
        private const val ROW_SHOW = 3

        //
        private const val TEST_ROW_INDEX = 4
    }

    init {
        mXPosition = m_iViewHolder!!.adapterPosition

        // find the view holder
        m_iViewHolder = m_iTableView.columnHeaderRecyclerView
            .findViewHolderForAdapterPosition(mXPosition) as ColumnHeaderViewHolder?
        initialize()
    }
}