package com.ipf.technicalrulesquiz.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.ipf.technicalrulesquiz.billing.BillingManager
import com.ipf.technicalrulesquiz.databinding.FragmentReviewBinding
import com.ipf.technicalrulesquiz.ui.quiz.QuizViewModel

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

        val result = viewModel.getQuizResult()
        val adapter = AnswerReviewAdapter(result.answeredQuestions)
        binding.answersRecyclerView.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (BuildConfig.SHOW_ADS && !BillingManager.isAdsRemoved(requireContext())) {
            binding.adView.loadAd(AdRequest.Builder().build())
        } else {
            binding.adView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
