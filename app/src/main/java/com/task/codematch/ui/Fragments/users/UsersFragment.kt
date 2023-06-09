package com.task.codematch.ui.Fragments.users

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
import com.task.codematch.ui.MainActivity
import com.task.codematch.R
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.databinding.FragmentUsersBinding
import com.task.codematch.utils.LastItemMarginDecoration
import com.task.codematch.utils.SnackBarUtils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel by viewModels<UsersViewModel>()
    private lateinit var userListAdapter: UsersListAdapter
    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userListAdapter = UsersListAdapter(mutableListOf<User>())
        binding.rvUsers.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.rvUsers.addItemDecoration(LastItemMarginDecoration(resources.getDimensionPixelSize(R.dimen.cell_bottom_margin)))

        viewModel.getAllUsers();

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
                                            R.id.action_usersFragment_to_userDetailFragment,
                                            bundle
                                        )
                                    }
                                    UserListItemBinding.ivFavorite.setOnClickListener {
                                        viewModel.toggleFavoriteValue(item)
                                        val animation = AnimationUtils.loadAnimation(
                                            context,
                                            R.anim.click_animation
                                        )
                                        it.startAnimation(animation)
                                        CoroutineScope(Dispatchers.Main).launch {
                                            delay(400)
                                            binding.rvUsers.adapter?.notifyItemChanged(position)
                                        }
                                    }
                                }
                            }
                            if (MainActivity.isAnimatedRecyclerView) {
                                val controller = AnimationUtils.loadLayoutAnimation(
                                    context,
                                    R.anim.layout_fall_down
                                )
                                binding.rvUsers.layoutAnimation = controller
                                binding.rvUsers.scheduleLayoutAnimation()
                                MainActivity.isAnimatedRecyclerView = false
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