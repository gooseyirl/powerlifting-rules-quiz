package com.gooseco.technicalrulesquiz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.databinding.FragmentQuizSetupBinding

class QuizSetupFragment : Fragment() {

    private var _binding: FragmentQuizSetupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.card10.setOnClickListener { startQuiz(10) }
        binding.card25.setOnClickListener { startQuiz(25) }
        binding.card50.setOnClickListener { startQuiz(50) }
    }

    private fun startQuiz(questionCount: Int) {
        findNavController().navigate(
            R.id.action_quiz_setup_to_quiz,
            bundleOf("questionCount" to questionCount)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
