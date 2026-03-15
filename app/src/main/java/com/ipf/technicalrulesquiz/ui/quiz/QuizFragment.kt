package com.ipf.technicalrulesquiz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.ipf.technicalrulesquiz.BuildConfig
import com.ipf.technicalrulesquiz.R
import com.ipf.technicalrulesquiz.billing.BillingManager
import com.ipf.technicalrulesquiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    private val radioButtons: List<RadioButton> by lazy {
        listOf(
            binding.option1,
            binding.option2,
            binding.option3,
            binding.option4
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startQuiz(10)
        setupObservers()
        setupListeners()

        if (!BuildConfig.SHOW_ADS || BillingManager.isAdsRemoved(requireContext())) {
            binding.adView.visibility = View.GONE
        }
    }

    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            binding.questionText.text = question.question

            question.options.forEachIndexed { index, option ->
                if (index < radioButtons.size) {
                    radioButtons[index].text = option
                    radioButtons[index].visibility = View.VISIBLE
                } else {
                    radioButtons.getOrNull(index)?.visibility = View.GONE
                }
            }

            binding.optionsGroup.clearCheck()
        }

        viewModel.currentQuestionNumber.observe(viewLifecycleOwner) { number ->
            val total = viewModel.totalQuestions.value ?: 0
            binding.questionCounter.text = getString(R.string.question_counter, number, total)

            val progress = if (total > 0) (number * 100) / total else 0
            binding.progressIndicator.setProgressCompat(progress, true)
        }

        viewModel.selectedAnswerIndex.observe(viewLifecycleOwner) { selectedIndex ->
            binding.btnNext.isEnabled = selectedIndex != null
        }

        viewModel.quizCompleted.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                findNavController().navigate(R.id.action_quiz_to_result)
            }
        }
    }

    private fun setupListeners() {
        radioButtons.forEachIndexed { index, radioButton ->
            radioButton.setOnClickListener {
                viewModel.selectAnswer(index)
            }
        }

        binding.btnNext.setOnClickListener {
            viewModel.submitAnswer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.SHOW_ADS && !BillingManager.isAdsRemoved(requireContext())) {
            binding.adView.loadAd(AdRequest.Builder().build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
