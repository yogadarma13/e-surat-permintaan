package com.example.e_suratpermintaan.presentation.tableview.holder

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.e_suratpermintaan.R

class RowHeaderViewHolder(p_jItemView: View) : AbstractViewHolder(p_jItemView) {
    val row_header_textview: TextView
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
        itemView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                nBackgroundColorId
            )
        )
        row_header_textview.setTextColor(
            ContextCompat.getColor(
                row_header_textview.context,
                nForegroundColorId
            )
        )
    }

    init {
        row_header_textview = p_jItemView.findViewById(R.id.row_header_textview)
    }
}