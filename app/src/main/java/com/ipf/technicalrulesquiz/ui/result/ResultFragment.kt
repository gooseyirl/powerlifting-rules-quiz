package com.ipf.technicalrulesquiz.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.ipf.technicalrulesquiz.R
import com.ipf.technicalrulesquiz.billing.BillingManager
import com.ipf.technicalrulesquiz.databinding.FragmentResultBinding
import com.ipf.technicalrulesquiz.ui.quiz.QuizViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = viewModel.getQuizResult()

        binding.resultTitle.text = getString(R.string.quiz_completed)

        binding.scorePercentage.text = getString(R.string.score_format, result.scorePercentage)

        binding.scoreDetails.text = getString(
            R.string.score_details_format,
            result.correctAnswers,
            result.totalQuestions
        )

        binding.correctCount.text = result.correctAnswers.toString()
        binding.incorrectCount.text = result.incorrectAnswers.toString()

        binding.passStatus.text = if (result.passed) {
            getString(R.string.you_passed)
        } else {
            getString(R.string.you_failed)
        }

        binding.passStatus.setTextColor(
            if (result.passed) {
                requireContext().getColor(android.R.color.holo_green_dark)
            } else {
                requireContext().getColor(android.R.color.holo_orange_dark)
            }
        )

        binding.btnReviewAnswers.setOnClickListener {
            findNavController().navigate(R.id.action_result_to_review)
        }

        binding.btnRetakeQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_result_to_quiz)
        }

        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_result_to_home)
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
