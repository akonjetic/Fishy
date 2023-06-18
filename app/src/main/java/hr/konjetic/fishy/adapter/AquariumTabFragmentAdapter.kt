package hr.konjetic.fishy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.EXTRA_FISH
import hr.konjetic.fishy.activity.FishActivity
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.databinding.FishListAquariumItemBinding
import kotlinx.coroutines.runBlocking

class AquariumTabFragmentAdapter(
    private val context: Context,
    private val fishList: ArrayList<AquariumFish>,
    private var aquarium: Aquarium
) : RecyclerView.Adapter<AquariumTabFragmentAdapter.AquariumFishViewHolder>() {

    class AquariumFishViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = FishListAquariumItemBinding.bind(view)
    }

    private var editable = false

    @SuppressLint("NotifyDataSetChanged")
    fun updateFishAndAquarium(updated: ArrayList<AquariumFish>, aquarium : Aquarium){
        fishList.clear()
        fishList.addAll(updated)

        this.aquarium = aquarium

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEditable(){
        editable = !editable
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AquariumFishViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fish_list_aquarium_item, parent, false)
        return AquariumFishViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AquariumFishViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentFish = fishList[position]

        holder.binding.fishName.text = "${currentFish.name} (${currentFish.gender})"
        holder.binding.fishFamilyName.text = currentFish.fishFamily.name
        holder.binding.fishImage.load(currentFish.image)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, FishActivity::class.java).apply {
                putExtra(EXTRA_FISH, currentFish.toFish())
            }

            context.startActivity(intent)
        }

        if (editable){
            holder.binding.quantityET.visibility = View.VISIBLE
        } else{
            holder.binding.quantityET.visibility = View.GONE
        }

        holder.binding.quantityET.text = null
        holder.binding.quantityET.hint = currentFish.quantity.toString()
            holder.binding.quantityET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val chosenQuantity = s.toString().toIntOrNull()

                if (chosenQuantity != null) {
                    currentFish.quantity = chosenQuantity
                    fishList[position] = currentFish
                    aquarium.fish.clear()
                    aquarium.fish.addAll(fishList)

                    runBlocking {
                        FishDatabase.getDatabase(context)?.getFishDao()?.updateAquarium(aquarium)
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return fishList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int){
        aquarium.fish.remove(fishList[position])

        runBlocking {
            FishDatabase.getDatabase(context)?.getFishDao()?.updateAquarium(aquarium)
        }

        notifyItemRemoved(position)
        fishList.remove(fishList[position])
        notifyDataSetChanged()

    }
}