package com.example.e_suratpermintaan.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.e_suratpermintaan.presentation.viewholders.ViewHolderFactory

typealias InflateBaseAdapter<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

// https://stackoverflow.com/questions/51087357/converting-a-generic-recyclerview-adapter-to-kotlin
class BaseAdapter<T : BaseViewHolder, B: ViewBinding>(private val inflate: InflateBaseAdapter<B>, private val clazz: Class<T>) :
    RecyclerView.Adapter<BaseViewHolder>() {

    val itemList: ArrayList<Any> = arrayListOf()
    private var onItemClickListener: ((Any?, String?) -> Unit) = { _, _ -> }

    fun setOnItemClickListener(listener: (item: Any?, actionString: String?) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = inflate.invoke(LayoutInflater.from(parent.context), parent, false)
//            LayoutInflater.from(parent.context)
//            .inflate(layoutRes, parent, false)

        return ViewHolderFactory.create(view, clazz)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = itemList[holder.layoutPosition]
        holder.bind(data, position, onItemClickListener)
    }
}