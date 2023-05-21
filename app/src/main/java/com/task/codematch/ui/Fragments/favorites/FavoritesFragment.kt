package com.task.codematch.ui.Fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.codematch.Adapters.UsersListAdapter
import com.task.codematch.R
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.databinding.FragmentFavoritesBinding
import com.task.codematch.utils.LastItemMarginDecoration
import com.task.codematch.utils.SnackBarUtils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel by viewModels<FavoritesViewModel>()
    private lateinit var userListAdapter: UsersListAdapter
    private var _binding: FragmentFavoritesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userListAdapter = UsersListAdapter(mutableListOf<User>())
        binding.rvUsers.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.rvUsers.addItemDecoration(LastItemMarginDecoration(resources.getDimensionPixelSize(R.dimen.cell_bottom_margin)))

        viewModel.getFavoritesUsers();

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.users.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Success -> {
                        hideProgressBar()
                        if (data.data?.isEmpty() == true) {
                            binding.animNoUser.visibility = View.VISIBLE
                            binding.tvNoUsers.visibility = View.VISIBLE
                            binding.rvUsers.adapter = UsersListAdapter(mutableListOf<User>())
                        } else {
                            binding.animNoUser.visibility = View.INVISIBLE
                            binding.tvNoUsers.visibility = View.INVISIBLE
                            binding.rvUsers.visibility = View.VISIBLE
                            binding.rvUsers.layoutManager = LinearLayoutManager(context)
                            binding.rvUsers.adapter = data.data?.let {
                                UsersListAdapter(it) { UserListItemBinding, item, position ->
                                    UserListItemBinding.root.setOnClickListener {
                                        val bundle = Bundle()
                                        bundle.putLong("user_id", item.id)
                                        bundle.putParcelable("user", item)
                                        it.findNavController().navigate(
                                            R.id.action_favoritesFragment_to_userDetailFragment,
                                            bundle
                                        )
                                    }
                                    UserListItemBinding.ivFavorite.setOnClickListener {
                                        viewModel.unFavoriteValue(item)
                                        it.isEnabled = false
                                        val animation = AnimationUtils.loadAnimation(
                                            context,
                                            R.anim.click_animation
                                        )
                                        it.startAnimation(animation)
                                        CoroutineScope(Dispatchers.Main).launch {
                                            delay(500)
                                            if (position < data.data.size && binding != null) {
                                                data.data.removeAt(position)
                                                binding?.rvUsers?.adapter?.notifyDataSetChanged()
                                                it.isEnabled = true
                                                if (data.data.isEmpty()) {
                                                    binding?.animNoUser?.visibility = View.VISIBLE
                                                    binding?.tvNoUsers?.visibility = View.VISIBLE
                                                    binding?.rvUsers?.adapter =
                                                        UsersListAdapter(mutableListOf<User>())
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Failed -> {
                        hideProgressBar()
                        requireContext().showSnackBar(
                            rootView = binding.root,
                            message = data.message
                        )
                    }
                    else -> {}
                }
            }

        }

        return root
    }

    private fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}