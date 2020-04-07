package com.example.blackout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blackout.database.Party
import com.example.blackout.databinding.ListItemPartyBinding


class PartyAdapter(private val clickListener: PartyItemListener)
    : ListAdapter<Party, PartyAdapter.ViewHolder>(
    PartyDiffCallback()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor (private val binding: ListItemPartyBinding):
        RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPartyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }

        }

        fun bind(
            item: Party,
            clickListener: PartyItemListener
        ){
            binding.party = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}


class PartyDiffCallback : DiffUtil.ItemCallback<Party>(){

    override fun areItemsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem.partyId == newItem.partyId
    }

    override fun areContentsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem == newItem
    }

}


class PartyItemListener(val clickListener: (partyId: Long) -> Unit) {

    fun onClick(party: Party) = clickListener(party.partyId)

}