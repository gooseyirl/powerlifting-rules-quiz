package com.gooseco.technicalrulesquiz.ui.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gooseco.technicalrulesquiz.BuildConfig
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.billing.BillingManager
import com.gooseco.technicalrulesquiz.data.model.QuizQuestion
import com.gooseco.technicalrulesquiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()
    private var currentQuestion: QuizQuestion? = null

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

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val questionCount = arguments?.getInt("questionCount", 10) ?: 10
        viewModel.startQuiz(questionCount)
        setupObservers()
        setupListeners()

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

    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            currentQuestion = question
            binding.questionId.text = "#${question.id}"
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

        binding.reportQuestion.setOnClickListener {
            currentQuestion?.let { showReportDialog(it) }
        }
    }

    private fun showReportDialog(question: QuizQuestion) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.report_question_dialog_title)
            .setMessage(R.string.report_question_dialog_message)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.report_question_open_email) { _, _ ->
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_email_subject, question.id))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.report_email_body, question.id, question.question))
                }
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
            .show()
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
