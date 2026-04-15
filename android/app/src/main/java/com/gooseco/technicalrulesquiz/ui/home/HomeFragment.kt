package com.gooseco.technicalrulesquiz.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gooseco.technicalrulesquiz.BuildConfig
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.billing.BillingManager
import com.gooseco.technicalrulesquiz.databinding.FragmentHomeBinding

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

        val basePaddingTop = binding.root.paddingTop
        val basePaddingBottom = binding.root.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = basePaddingTop + bars.top, bottom = basePaddingBottom + bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.btnStartQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_quiz_setup)
        }

        binding.versionInfo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rules_book_url)))
            startActivity(intent)
        }

        binding.suggestQuestion.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.suggest_question_dialog_title)
                .setMessage(R.string.suggest_question_dialog_message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.report_question_open_email) { _, _ ->
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.suggest_email_subject))
                        putExtra(Intent.EXTRA_TEXT, getString(R.string.suggest_email_body))
                    }
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    }
                }
                .show()
        }

        setupRemoveAdsButton()
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
