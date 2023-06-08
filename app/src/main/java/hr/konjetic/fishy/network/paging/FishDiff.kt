package hr.konjetic.fishy.network.paging

import androidx.recyclerview.widget.DiffUtil
import hr.konjetic.fishy.network.model.Fish
import hr.konjetic.fishy.network.model.FishResponse

object FishDiff : DiffUtil.ItemCallback<Fish>() {
    override fun areItemsTheSame(oldItem: Fish, newItem: Fish): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Fish, newItem: Fish): Boolean {
        return oldItem == newItem
    }
}