package com.dumber.study.dataBinding

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.dumber.study.databinding.RowUserItemBinding

class UserViewHolder(itemView: View, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder(itemView) {
    val binding = RowUserItemBinding.bind(itemView)
    init {
        binding.lifecycleOwner = lifecycleOwner
    }

    var user: UserData? = null

    fun bindData(user: UserData) {
        this.user = user.also {
            binding.user = it
        }
    }
}