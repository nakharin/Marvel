package com.nakharin.marvel.presentation.content.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakharin.marvel.R
import com.nakharin.marvel.utils.delegate.OnItemClickListener
import com.nakharin.marvel.utils.extension.loadImage
import kotlinx.android.synthetic.main.view_holder_content_body.view.*

class ContentBodyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): ContentBodyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_holder_content_body, parent, false)
            return ContentBodyViewHolder(view)
        }
    }

    fun bind(position: Int, url: String, onItemClickListener: OnItemClickListener<String>?) {
        itemView.contentBodyImgPhoto.loadImage(url)

        itemView.contentBodyCrdRoot.setOnClickListener {
            onItemClickListener?.invoke(it, url, position)
        }
    }
}