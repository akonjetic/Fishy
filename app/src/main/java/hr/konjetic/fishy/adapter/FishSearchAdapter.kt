package hr.konjetic.fishy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.EXTRA_FAVORITE
import hr.konjetic.fishy.activity.EXTRA_FISH
import hr.konjetic.fishy.activity.FishActivity
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.databinding.SearchFishItemBinding
import hr.konjetic.fishy.network.model.Fish
import kotlinx.coroutines.runBlocking

class FishSearchAdapter(
    private val context: Context,
    private val fishList: ArrayList<Fish>
) : RecyclerView.Adapter<FishSearchAdapter.FishSearchViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateFishList(updated: ArrayList<Fish>){
        fishList.clear()
        fishList.addAll(updated)
        notifyDataSetChanged()
    }

    class FishSearchViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = SearchFishItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_fish_item, parent, false)
        return FishSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: FishSearchViewHolder, position: Int) {
        val fish = fishList[position]
        holder.binding.searchFishName.text = "${fish.name} (${fish.gender})"


        holder.binding.root.setOnClickListener {

            var favorite: Boolean

            val sharedPreferences =
                context.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 1)

            runBlocking {
                val favFish = FishDatabase.getDatabase(context)?.getFishDao()?.getFavoriteFishByUserAndFish(userId = userId, fishId = fish.id)
                favorite = favFish!!.isNotEmpty()
            }

            val intent = Intent(context, FishActivity::class.java).apply {
                putExtra(EXTRA_FISH, fish)
                putExtra(EXTRA_FAVORITE, favorite)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (fishList.size > 8) {
            8
        } else {
            fishList.size
        }
    }


}