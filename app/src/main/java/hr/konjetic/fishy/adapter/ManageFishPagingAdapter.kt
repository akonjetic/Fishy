package hr.konjetic.fishy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.konjetic.fishy.R
import hr.konjetic.fishy.databinding.ManageFishListItemBinding
import hr.konjetic.fishy.network.model.Fish

class ManageFishPagingAdapter(
    private val context: Context,
    diffCallback: DiffUtil.ItemCallback<Fish>
) : PagingDataAdapter<Fish, ManageFishPagingAdapter.HomeFishViewHolder>(diffCallback) {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    class HomeFishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ManageFishListItemBinding.bind(view)
    }

    override fun onBindViewHolder(holder: HomeFishViewHolder, position: Int) {
        val fish = getItem(position)

        fish?.let {
            holder.binding.fishImage.load(it.image)
            holder.binding.fishFamilyName.text = it.fishFamily.name
            holder.binding.fishName.text = it.name

            holder.binding.fishDeleteIcon.setOnClickListener {
                onDeleteClickListener?.onDeleteClicked(fish)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFishViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.manage_fish_list_item, parent, false)
        return HomeFishViewHolder(view)
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(item: Fish)
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }
}
