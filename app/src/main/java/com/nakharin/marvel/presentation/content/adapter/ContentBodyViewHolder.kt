package com.nakharin.marvel.presentation.content.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakharin.marvel.R
import com.nakharin.marvel.`typealias`.OnItemClickListener
import com.nakharin.marvel.extension.load
import kotlinx.android.synthetic.main.view_holder_content_body.view.*

class ContentBodyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): ContentBodyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_holder_content_body, parent, false)
            return ContentBodyViewHolder(view)
        }
    }

    fun bind(position: Int, url: String, mOnItemClickListener: OnItemClickListener<String>?) {
        itemView.contentBodyImgPhoto.load(url)

        itemView.contentBodyCrdRoot.setOnClickListener {
            mOnItemClickListener?.invoke(it, url, position)
        }
    }
}