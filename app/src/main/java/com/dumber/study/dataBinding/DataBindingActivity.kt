package com.dumber.study.dataBinding

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dumber.study.R
import com.dumber.study.databinding.ActivityDataBindingBinding

class UserData(val name: String, val imageUrl: String)

class DataBindingActivity: AppCompatActivity() {
    lateinit var binding: ActivityDataBindingBinding
    val userList = arrayOf(
        UserData("정건국", "https://encrypted-tbn3.gstatic.com/licensed-image?q=tbn:ANd9GcTNvy3EgAN5pnP4p8xPVM0_lI8d7Ql9ZResHJJIGfwXDH70GkZbvnMIsFXG7ZyX_YO9lpX4SLplVlgqQWw")
        , UserData("정숙희", "https://img1.daumcdn.net/thumb/R1280x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/h3hK/image/YL8gNeSSlRcBk9lQ-c-8_HR9ehc.png")
        , UserData("정소은", "https://www.gfound.org/data/file/together/237479870_z0DTblpE_cee4fb8cc8eecbd2542180e80d961a473177168f.jpg")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding)
        binding.lifecycleOwner = this
        binding.rvList.adapter = object: RecyclerView.Adapter<UserViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): UserViewHolder {
                return UserViewHolder(layoutInflater.inflate(R.layout.row_user_item, parent, false), this@DataBindingActivity)
            }

            override fun onBindViewHolder(
                holder: UserViewHolder,
                position: Int
            ) {
                holder.bindData(userList[position])
            }

            override fun getItemCount(): Int {
                return userList.size
            }
        }
    }

}