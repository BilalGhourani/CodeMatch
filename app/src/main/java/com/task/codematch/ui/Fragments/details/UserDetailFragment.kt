package com.task.codematch.ui.Fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.task.codematch.MainActivity
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userDetailViewModel =
            ViewModelProvider(this).get(UserDetailViewModel::class.java)

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user_id = arguments?.getLong("user_id")
        if (user_id != null) {
            userDetailViewModel.getUserDetail(user_id)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            userDetailViewModel._uiState.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Resource.Success -> {
                        binding.nameTextVal.text = data.data.name
                        binding.usernameTextVal.text = data.data.username
                        binding.emailTextVal.text = data.data.email
                        binding.phoneTextVal.text = data.data.phone
                        binding.websiteTextVal.text = data.data.website

                        binding.addressStreetTextVal.text = data.data.address.street
                        binding.addressSuiteTextVal.text = data.data.address.suite
                        binding.addressCityTextVal.text = data.data.address.city
                        binding.addressZipcodeTextVal.text = data.data.address.zipcode
                        binding.addressGeoTextVal.text =
                            data.data.address.geo.lat.toString() + " - " + data.data.address.geo.lng

                        binding.companyNameTextVal.text = data.data.company.cname
                        binding.companyCatchPhraseTextVal.text = data.data.company.catchPhrase
                        binding.companyBsTextVal.text = data.data.company.bs

                        postponeEnterTransition()
                        view?.viewTreeObserver?.addOnPreDrawListener {
                            startPostponedEnterTransition()
                            true
                        }
                    }
                    else -> {}
                }
            }
        }
//        (context as MainActivity).hideBottomNavigation()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        (context as MainActivity).showBottomNavigation()
        _binding = null
    }
}