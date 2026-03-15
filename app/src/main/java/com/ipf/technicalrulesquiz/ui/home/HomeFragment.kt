package com.ipf.technicalrulesquiz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ipf.technicalrulesquiz.BuildConfig
import com.ipf.technicalrulesquiz.R
import com.ipf.technicalrulesquiz.billing.BillingManager
import com.ipf.technicalrulesquiz.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var billingManager: BillingManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnQuiz10.setOnClickListener { startQuiz(10) }
        binding.btnQuiz25.setOnClickListener { startQuiz(25) }
        binding.btnQuiz50.setOnClickListener { startQuiz(50) }

        setupRemoveAdsButton()
    }

    private fun startQuiz(questionCount: Int) {
        findNavController().navigate(
            R.id.action_home_to_quiz,
            bundleOf("questionCount" to questionCount)
        )
    }

    private fun setupRemoveAdsButton() {
        // Only show the Remove Ads button when ads are enabled
        if (!BuildConfig.SHOW_ADS) {
            binding.btnRemoveAds.visibility = View.GONE
            return
        }

        if (BillingManager.isAdsRemoved(requireContext())) {
            showAdsRemovedState()
            return
        }

        billingManager = BillingManager(requireActivity()) {
            showAdsRemovedState()
        }
        billingManager?.connect()

        binding.btnRemoveAds.setOnClickListener {
            billingManager?.launchPurchaseFlow()
        }
    }

    private fun showAdsRemovedState() {
        binding.btnRemoveAds.text = getString(R.string.ads_removed)
        binding.btnRemoveAds.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        billingManager?.disconnect()
        billingManager = null
        _binding = null
    }
}
