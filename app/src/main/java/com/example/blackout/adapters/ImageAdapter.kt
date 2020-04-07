package com.example.blackout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blackout.database.PartyImage
import com.example.blackout.databinding.ListImagePartyBinding


class ImageAdapter(private val clickListener: ImageItemListener): ListAdapter<PartyImage, ImageAdapter.ViewHolder>(
    ImageDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor (private val binding: ListImagePartyBinding):
        RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListImagePartyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }

        }

        fun bind(
            item: PartyImage,
            clickListener: ImageItemListener
        ){
            binding.image = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}


class ImageDiffCallBack : DiffUtil.ItemCallback<PartyImage>(){

    override fun areItemsTheSame(oldItem: PartyImage, newItem: PartyImage): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: PartyImage, newItem: PartyImage): Boolean {
        return oldItem == newItem
    }

}


class ImageItemListener(val clickListener: (image: PartyImage) -> Unit) {

    fun onClick(image: PartyImage) = clickListener(image)

}