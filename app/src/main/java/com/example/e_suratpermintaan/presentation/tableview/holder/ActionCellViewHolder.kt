package com.example.e_suratpermintaan.presentation.tableview.holder

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.tableview.model.CellModel

class ActionCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val cell_textview: TextView
    val cell_container: LinearLayout
    fun setCellModel(p_jModel: CellModel, pColumnPosition: Int) {

        // Change textView align by column
        cell_textview.gravity = ColumnHeaderViewHolder.COLUMN_TEXT_ALIGNS.get(pColumnPosition) or
                Gravity.CENTER_VERTICAL

        // Set text
        cell_textview.text = p_jModel.data.toString()

        // It is necessary to remeasure itself.
        cell_container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        cell_textview.requestLayout()
    }

    override fun setSelected(p_nSelectionState: SelectionState) {
        super.setSelected(p_nSelectionState)
        if (p_nSelectionState == SelectionState.SELECTED) {
            cell_textview.setTextColor(
                ContextCompat.getColor(
                    cell_textview.context,
                    R.color.selected_text_color
                )
            )
        } else {
            cell_textview.setTextColor(
                ContextCompat.getColor(
                    cell_textview.context,
                    R.color.unselected_text_color
                )
            )
        }
    }

    init {
        cell_textview = itemView.findViewById(R.id.cell_data)
        cell_container = itemView.findViewById(R.id.cell_container)
    }
}