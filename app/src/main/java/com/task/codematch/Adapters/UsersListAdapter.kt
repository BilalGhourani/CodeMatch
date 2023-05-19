package com.task.codematch.Adapters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.task.codematch.R
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.databinding.UserListItemBinding


class UsersListAdapter(
    private val users: MutableList<User>,
    private val onItemClickListener: (UserListItemBinding, User, Int) -> Unit = { _: UserListItemBinding, _: User, Int -> }
) :
    RecyclerView.Adapter<UsersListAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position], position)
    }

    override fun getItemCount() = users.size

    inner class UsersViewHolder(val binding: UserListItemBinding, pos: Int) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, pos: Int) {
            binding.userName.text = item.name
            binding.userEmail.text = item.email
            if (item.isFavorite == 1) {
                binding.ivFavorite.setImageResource(R.drawable.favorite)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.unfavorite)
            }
            onItemClickListener(binding, item, pos)
        }
    }
}