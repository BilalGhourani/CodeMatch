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
    private val users: List<User>,
    private val onItemClickListener: (UserListItemBinding, User) -> Unit = { _: UserListItemBinding, _: User -> }
) :
    RecyclerView.Adapter<UsersListAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {

        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(createOnClickListener(users[position]), users[position])
    }

    override fun getItemCount() = users.size

    inner class UsersViewHolder(val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: User) {
            binding.userName.text = item.name
            binding.userEmail.text = item.email
            if (item.isFavorite==1) {
                binding.ivFavorite.setImageResource(R.drawable.favorite)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.unfavorite)
            }

            ViewCompat.setTransitionName(binding.userName, "name_${item.id}")
            ViewCompat.setTransitionName(binding.userEmail, "email_${item.id}")

            binding.root.setOnClickListener(listener)
            onItemClickListener(binding, item)
        }
    }

    private fun createOnClickListener(
        user: User
    ): View.OnClickListener {
        return View.OnClickListener {
            val bundle = Bundle()
            bundle.putLong("user_id", user.id)
            it.findNavController().navigate(R.id.action_usersFragment_to_userDetailFragment, bundle)
        }
    }
}