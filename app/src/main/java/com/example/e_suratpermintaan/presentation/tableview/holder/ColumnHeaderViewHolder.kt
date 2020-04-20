package com.example.e_suratpermintaan.presentation.tableview.holder

import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.tableview.model.ColumnHeaderModel


class ColumnHeaderViewHolder(itemView: View, val tableView: ITableView) :
    AbstractSorterViewHolder(itemView) {
    val column_header_container: LinearLayout
    val column_header_textview: TextView
    val column_header_sort_button: ImageButton
    fun setColumnHeaderModel(
        pColumnHeaderModel: ColumnHeaderModel,
        pColumnPosition: Int
    ) {

        // Change alignment of textView
        column_header_textview.gravity =
            COLUMN_TEXT_ALIGNS[pColumnPosition] or Gravity.CENTER_VERTICAL

        // Set text data
        column_header_textview.text = pColumnHeaderModel.data

        // It is necessary to remeasure itself.
        column_header_container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        column_header_textview.requestLayout()
    }

    override fun setSelected(p_nSelectionState: SelectionState) {
        super.setSelected(p_nSelectionState)
        val nBackgroundColorId: Int
        val nForegroundColorId: Int
        if (p_nSelectionState == SelectionState.SELECTED) {
            nBackgroundColorId = R.color.selected_background_color
            nForegroundColorId = R.color.selected_text_color
        } else if (p_nSelectionState == SelectionState.UNSELECTED) {
            nBackgroundColorId = R.color.unselected_header_background_color
            nForegroundColorId = R.color.unselected_text_color
        } else { // SelectionState.SHADOWED
            nBackgroundColorId = R.color.shadow_background_color
            nForegroundColorId = R.color.unselected_text_color
        }
        column_header_container.setBackgroundColor(
            ContextCompat.getColor(
                column_header_container
                    .context, nBackgroundColorId
            )
        )
        column_header_textview.setTextColor(
            ContextCompat.getColor(
                column_header_container
                    .context, nForegroundColorId
            )
        )
    }

    override fun onSortingStatusChanged(pSortState: SortState) {
        super.onSortingStatusChanged(pSortState)

        // It is necessary to remeasure itself.
        column_header_container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        controlSortState(pSortState)
        column_header_textview.requestLayout()
        column_header_sort_button.requestLayout()
        column_header_container.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(pSortState: SortState) {
        if (pSortState == SortState.ASCENDING) {
            column_header_sort_button.visibility = View.VISIBLE
            column_header_sort_button.setImageResource(R.drawable.ic_down)
        } else if (pSortState == SortState.DESCENDING) {
            column_header_sort_button.visibility = View.VISIBLE
            column_header_sort_button.setImageResource(R.drawable.ic_up)
        } else {
            column_header_sort_button.visibility = View.GONE
        }
    }

    private val mSortButtonClickListener: View.OnClickListener = View.OnClickListener {
        if (sortState == SortState.ASCENDING) {
            tableView.sortColumn(adapterPosition, SortState.DESCENDING)
        } else if (sortState == SortState.DESCENDING) {
            tableView.sortColumn(adapterPosition, SortState.ASCENDING)
        } else {
            // Default one
            tableView.sortColumn(adapterPosition, SortState.DESCENDING)
        }
    }

    companion object {
        val COLUMN_TEXT_ALIGNS = intArrayOf( // Id
            Gravity.CENTER,  // Name
            Gravity.LEFT,  // Nickname
            Gravity.LEFT,  // Email
            Gravity.LEFT,  // BirthDay
            Gravity.CENTER,  // Gender (Sex)
            Gravity.CENTER,  // Age
            Gravity.CENTER,  // Job
            Gravity.LEFT,  // Salary
            Gravity.CENTER,  // CreatedAt
            Gravity.CENTER,  // UpdatedAt
            Gravity.CENTER,  // Address
            Gravity.LEFT,  // Zip Code
            Gravity.RIGHT,  // Phone
            Gravity.RIGHT,  // Fax
            Gravity.RIGHT
        )
    }

    init {
        column_header_textview = itemView.findViewById(R.id.column_header_textView)
        column_header_container = itemView.findViewById(R.id.column_header_container)
        column_header_sort_button = itemView.findViewById(R.id.column_header_sort_imageButton)

        // Set click listener to the sort button
        column_header_sort_button.setOnClickListener(mSortButtonClickListener)
    }
}