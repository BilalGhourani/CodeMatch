package com.task.codematch.ui.Fragments.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.task.codematch.ui.MainActivity
import com.task.codematch.R
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    private val viewModel by viewModels<UserDetailViewModel>()
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var _user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var user = arguments?.getParcelable<User>("user")
        if (user != null) {
            viewModel.notifyModel(user)
        } else {
            val user_id = arguments?.getLong("user_id")
            if (user_id != null) {
                viewModel.getUserDetail(user_id)
            }
        }

        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavoriteValue(_user)
            val animation = AnimationUtils.loadAnimation(
                context,
                R.anim.click_animation
            )
            it.startAnimation(animation)
            CoroutineScope(Dispatchers.Main).launch {
                delay(400)
                if (_user.isFavorite == 1) {
                    binding.ivFavorite.setImageResource(R.drawable.favorite)
                } else {
                    binding.ivFavorite.setImageResource(R.drawable.unfavorite)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {

            viewModel._uiState.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Resource.Success -> {
                        _user = data.data
                        binding.parentCardview.visibility = View.VISIBLE
                        binding.nameTextVal.text = data.data.name
                        binding.usernameTextVal.text = data.data.username
                        binding.emailTextVal.text = data.data.email
                        binding.phoneTextVal.text = data.data.phone
                        binding.websiteTextVal.text = data.data.website

                        binding.addressStreetTextVal.text = data.data.address.street
                        binding.addressSuiteTextVal.text = data.data.address.suite
                        binding.addressCityTextVal.text = data.data.address.city
                        binding.addressZipcodeTextVal.text = data.data.address.zipcode
                        val geoString = String.format(
                            "Lat: %.6f, Lng: %.6f",
                            data.data.address.geo.lat,
                            data.data.address.geo.lng
                        )
                        binding.addressGeoTextVal.text = geoString

                        binding.companyNameTextVal.text = data.data.company.cname
                        binding.companyCatchPhraseTextVal.text = data.data.company.catchPhrase
                        binding.companyBsTextVal.text = data.data.company.bs
                        if (data.data.isFavorite == 1) {
                            binding.ivFavorite.setImageResource(R.drawable.favorite)
                        } else {
                            binding.ivFavorite.setImageResource(R.drawable.unfavorite)
                        }

                    }else -> {
                        binding.parentCardview.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context;
            mainActivity?.hideBottomNavigation()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity?.showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}