package com.example.e_suratpermintaan.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.e_suratpermintaan.presentation.viewholders.ViewHolderFactory

class BaseFilterableAdapter<T : BaseViewHolder>(
    private val layoutRes: Int,
    private val clazz: Class<T>
) :
    RecyclerView.Adapter<BaseViewHolder>(), Filterable {

    var oldItemList: ArrayList<Any> = arrayListOf()
    var itemList: ArrayList<Any> = arrayListOf()

    private lateinit var onItemClickListener: (item: Any?, actionString: String?) -> Unit

    fun setOnItemClickListener(listener: (item: Any?, actionString: String?) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        return ViewHolderFactory.create(view, clazz)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = itemList[holder.layoutPosition]
        holder.bind(data, position, onItemClickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                itemList = if (charString.isEmpty()) {
                    oldItemList
                } else {
                    val filteredList: ArrayList<Any> = arrayListOf()
                    for (row in oldItemList) {
                        if (row.toString().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = itemList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                itemList = filterResults.values as ArrayList<Any>
                notifyDataSetChanged()
            }
        }
    }
}