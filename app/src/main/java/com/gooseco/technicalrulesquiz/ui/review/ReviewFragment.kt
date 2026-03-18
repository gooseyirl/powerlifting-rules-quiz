package com.gooseco.technicalrulesquiz.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.gooseco.technicalrulesquiz.BuildConfig
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.billing.BillingManager
import com.gooseco.technicalrulesquiz.databinding.FragmentReviewBinding
import com.gooseco.technicalrulesquiz.ui.quiz.QuizViewModel

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = bars.top
            }
            binding.root.updatePadding(bottom = bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val result = viewModel.getQuizResult()
        val adapter = AnswerReviewAdapter(result.answeredQuestions) { pageNumber ->
            findNavController().navigate(
                R.id.action_review_to_pdf,
                bundleOf("pageNumber" to pageNumber)
            )
        }
        binding.answersRecyclerView.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (BuildConfig.SHOW_ADS && !BillingManager.isAdsRemoved(requireContext())) {
            MobileAds.initialize(requireContext()) {
                if (_binding != null) {
                    binding.adView.loadAd(AdRequest.Builder().build())
                }
            }
        } else {
            binding.adView.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.SHOW_ADS && !BillingManager.isAdsRemoved(requireContext())) {
            binding.adView.resume()
        }
    }

    override fun onPause() {
        if (BuildConfig.SHOW_ADS && !BillingManager.isAdsRemoved(requireContext())) {
            binding.adView.pause()
        }
        super.onPause()
    }

    override fun onDestroyView() {
        binding.adView.destroy()
        super.onDestroyView()
        _binding = null
    }
}
