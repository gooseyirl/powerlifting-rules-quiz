package com.ipf.technicalrulesquiz.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
