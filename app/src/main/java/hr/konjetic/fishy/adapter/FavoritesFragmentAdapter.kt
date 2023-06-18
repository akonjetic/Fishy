package hr.konjetic.fishy.adapter

import android.annotation.SuppressLint
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
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.databinding.FishListItemBinding
import kotlinx.coroutines.runBlocking

class FavoritesFragmentAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<FavoriteFish>
): RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesViewHolder>(){

    class FavoritesViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binding = FishListItemBinding.bind(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorites(updatedList: ArrayList<FavoriteFish>){
        favoritesList.clear()
        favoritesList.addAll(updatedList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fish_list_item, parent, false)
        return FavoritesViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {

        val fish = favoritesList[position]

        holder.binding.fishFavoriteIcon.isActivated = true

        holder.binding.fishFavoriteIcon.setOnClickListener {
            if (it.isActivated){
                runBlocking {
                    FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishWithCascade(favFishId = fish.fishId.toLong(), waterTypeId = fish.waterType.id, fishFamilyId = fish.fishFamily.id, habitatId = fish.habitat.id)
                }
                it.isActivated = false
                updateFavorites(
                    favoritesList.filter { data ->
                        data.id != fish.id
                    } as ArrayList<FavoriteFish>
                )
                notifyItemChanged(position)
            } else{
                runBlocking {
                    FishDatabase.getDatabase(context)?.getFishDao()?.insertFavoriteFish(fish)
                }
                it.isActivated = true
            }
        }


        holder.binding.fishImage.load(favoritesList[position].image)
        holder.binding.fishFamilyName.text = favoritesList[position].fishFamily.name
        holder.binding.fishName.text = "${fish.name} (${fish.gender})"

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, FishActivity::class.java).apply {
                putExtra(EXTRA_FISH, favoritesList[position].toFishResponseData())
                putExtra(EXTRA_FAVORITE, holder.binding.fishFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }
}