package hr.konjetic.fishy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.EXTRA_FAVORITE
import hr.konjetic.fishy.activity.EXTRA_FISH
import hr.konjetic.fishy.activity.FishActivity
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.databinding.FishListItemBinding

class AquariumTabFragmentAdapter(
    private val context: Context,
    private val fishList: ArrayList<AquariumFish>
) : RecyclerView.Adapter<AquariumTabFragmentAdapter.AquariumFishViewHolder>() {

    class AquariumFishViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = FishListItemBinding.bind(view)
    }

    fun updateFish(updated: ArrayList<AquariumFish>){
        fishList.clear()
        fishList.addAll(updated)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AquariumFishViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fish_list_item, parent, false)
        return AquariumFishViewHolder(view)
    }

    override fun onBindViewHolder(holder: AquariumFishViewHolder, position: Int) {
        val currentFish = fishList[position]

        holder.binding.fishName.text = currentFish.name
        holder.binding.fishFamilyName.text = currentFish.fishFamily.name
        holder.binding.fishImage.load(currentFish.image)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, FishActivity::class.java).apply {
                putExtra(EXTRA_FISH, currentFish.toFish())
                putExtra(EXTRA_FAVORITE, holder.binding.fishFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return fishList.size
    }
}