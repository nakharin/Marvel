package com.nakharin.marvel.presentation.content.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakharin.marvel.utils.delegate.OnItemClickListener

class ContentAdapter : RecyclerView.Adapter<ContentBodyViewHolder>() {

    private var images: List<String> = arrayListOf()

    private var mOnItemClickListener: OnItemClickListener<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentBodyViewHolder {
        return ContentBodyViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ContentBodyViewHolder, position: Int) {
        val imageUrl = images[position]
        holder.bind(position, imageUrl, mOnItemClickListener)
    }

    fun addAllItem(images: ArrayList<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    fun setOnItemClickLister(onItemClickListener: OnItemClickListener<String>) {
        mOnItemClickListener = onItemClickListener
    }
}
