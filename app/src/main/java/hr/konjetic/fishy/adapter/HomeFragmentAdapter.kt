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
import hr.konjetic.fishy.database.entities.FavoriteFishFamily
import hr.konjetic.fishy.database.entities.FavoriteHabitat
import hr.konjetic.fishy.database.entities.FavoriteWaterType
import hr.konjetic.fishy.databinding.FishListItemBinding
import hr.konjetic.fishy.databinding.FragmentFavoritesBinding
import hr.konjetic.fishy.network.model.Fish
import kotlinx.coroutines.runBlocking

class HomeFragmentAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<Fish>
): RecyclerView.Adapter<HomeFragmentAdapter.FavoritesViewHolder>(){

    class FavoritesViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binding = FishListItemBinding.bind(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorites(updatedList: ArrayList<Fish>){
        favoritesList.clear()
        favoritesList.addAll(updatedList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fish_list_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val fish = favoritesList[position]

        val sharedPreferences =
            context.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        runBlocking {
            val favFish = FishDatabase.getDatabase(context)?.getFishDao()?.getFavoriteFishByUserAndFish(userId = userId, fishId = favoritesList[position].id)

            holder.binding.fishFavoriteIcon.isActivated = favFish!!.isNotEmpty()
        }

        holder.binding.fishImage.load(favoritesList[position].image)
        holder.binding.fishFamilyName.text = favoritesList[position].fishFamily.name
        holder.binding.fishName.text = favoritesList[position].name

        holder.binding.fishFavoriteIcon.setOnClickListener {
            if (it.isActivated){
                runBlocking {
                    FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishWithCascade(favFishId = fish.id.toLong(), waterTypeId = fish.waterType.id, fishFamilyId = fish.fishFamily.id, habitatId = fish.habitat.id)
                }
                it.isActivated = false
            } else{
                runBlocking {
                    FishDatabase.getDatabase(context)?.getFishDao()?.insertFavoriteFish(FavoriteFish(0, userId, fish.id, fish.name, fish.description, FavoriteWaterType(fish.waterType.id, fish.waterType.type),
                        FavoriteFishFamily(fish.fishFamily.id, fish.fishFamily.name), FavoriteHabitat(fish.habitat.id, fish.habitat.name), fish.image, fish.minSchoolSize, fish.avgSchoolSize, fish.MinAquariumSizeInL,
                    fish.gender, fish.maxNumberOfSameGender, fish.availableInStore))
                }
                it.isActivated = true
            }
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, FishActivity::class.java).apply {
                putExtra(EXTRA_FISH, favoritesList[position])
                putExtra(EXTRA_FAVORITE, holder.binding.fishFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }
}