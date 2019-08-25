package com.nakharin.marvel.presentation.content.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakharin.marvel.`typealias`.OnItemClickListener

class ContentAdapter : RecyclerView.Adapter<ContentBodyViewHolder>() {

    private val mImages: ArrayList<String> = arrayListOf()

    private var mOnItemClickListener: OnItemClickListener<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentBodyViewHolder {
        return ContentBodyViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return mImages.size
    }

    override fun onBindViewHolder(holder: ContentBodyViewHolder, position: Int) {
        val imageUrl = mImages[position]
        holder.bind(position, imageUrl, mOnItemClickListener)
    }

    fun addAllItem(images: ArrayList<String>) {
        mImages.addAll(images)
        notifyDataSetChanged()
    }

    fun setOnItemClickLister(onItemClickListener: OnItemClickListener<String>) {
        mOnItemClickListener = onItemClickListener
    }
}