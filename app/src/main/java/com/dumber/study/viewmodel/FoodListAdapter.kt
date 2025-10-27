package com.dumber.study.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dumber.study.R
import com.dumber.study.databinding.RowFoodItemBinding
import com.dumber.study.viewmodel.model.Food

class FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val binding: RowFoodItemBinding = RowFoodItemBinding.bind(itemView)
}

class FoodListAdapter(val onMore: (() -> Unit)?): RecyclerView.Adapter<FoodViewHolder>() {

    val items = mutableListOf<Food>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_food_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: FoodViewHolder,
        position: Int
    ) {
        val food = items[position]
        holder.binding.tvTitle.text = food.title
        holder.binding.tvContent.text = food.content
        Glide.with(holder.binding.ivImage)
            .load(food.imgUrl)
            .into(holder.binding.ivImage)

        if (position >= items.size - 5)
            onMore?.also { it() }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(state: FoodListState) {
        when(state.event) {
            FoodListEvent.List -> {
                items.clear()
                items.addAll(state.items)
                notifyDataSetChanged()
            }
            FoodListEvent.MoreList -> {
                items.addAll(state.items)
                notifyDataSetChanged()
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int = items.size


}
