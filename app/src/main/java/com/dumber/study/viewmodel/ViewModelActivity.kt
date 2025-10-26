package com.dumber.study.viewmodel

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dumber.study.R
import com.dumber.study.databinding.ActivityViewmodelBinding
import com.dumber.study.databinding.RowFoodItemBinding
import com.dumber.study.viewmodel.model.FoodItem

class ViewModelActivity: AppCompatActivity() {

    lateinit var binding: ActivityViewmodelBinding
    var foods: List<FoodItem>? = null

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var foodItem: FoodItem? = null
        val itemBinding = RowFoodItemBinding.bind(itemView)

        fun bindData(position: Int) {
            foodItem = foods!![position]?.also {
                itemBinding.tvTitle.text = it.title
                itemBinding.tvContent.text = it.content
                Glide.with(this@ViewModelActivity)
                    .load(it.imgUrl)
                    .into(itemBinding.ivImage)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewmodelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodViewModel = ViewModelProvider.create(this).get(FoodListViewModel::class.java)
        foodViewModel.foodList.observe(this) {
            foods = it
            initAdapter()
        }
    }

    fun initAdapter() {
        binding.listView.adapter = object : RecyclerView.Adapter<ItemViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ItemViewHolder {
                return ItemViewHolder(layoutInflater.inflate(R.layout.row_food_item, parent, false))
            }

            override fun onBindViewHolder(
                holder: ItemViewHolder,
                position: Int
            ) {
                holder.bindData(position)
            }

            override fun getItemCount(): Int {
                return foods?.size ?: 0
            }

        }
    }

}