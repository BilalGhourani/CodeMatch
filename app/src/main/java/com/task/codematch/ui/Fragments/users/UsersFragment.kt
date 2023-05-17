package com.task.codematch.ui.Fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.codematch.Adapters.UsersListAdapter
import com.task.codematch.MainActivity
import com.task.codematch.R
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.databinding.FragmentUsersBinding
import com.task.codematch.utils.SnackBarUtils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
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

        userListAdapter = UsersListAdapter(emptyList())
        binding.rvUsers.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.users.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Success -> {
                        hideProgressBar()
                        if (data.data?.isEmpty() == true) {
                            binding.animNoUser.visibility = View.VISIBLE
                            binding.tvNoUsers.visibility = View.VISIBLE
                            binding.rvUsers.adapter = UsersListAdapter(emptyList())
                        } else {
                            binding.animNoUser.visibility = View.INVISIBLE
                            binding.tvNoUsers.visibility = View.INVISIBLE
                            binding.rvUsers.layoutManager = LinearLayoutManager(context)
                            binding.rvUsers.adapter = data.data?.let {
                                UsersListAdapter(it) { taskItemBinding, item ->
                                    taskItemBinding.ivFavorite.setOnClickListener {
                                        viewModel.markUserAsFavorite(item)
                                        requireContext().showSnackBar(
                                            rootView = binding.root,
                                            message = "Deleted",
                                        )
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
                            //for return animation
                            postponeEnterTransition()
                            view?.viewTreeObserver?.addOnPreDrawListener {
                                startPostponedEnterTransition()
                                true
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

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
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